package com.techshare.services.sale.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techshare.https.response.SaleDTO;
import com.techshare.mappers.sale.ConvertSale;
import com.techshare.entities.Sale;
import com.techshare.entities.Material;
import com.techshare.entities.UserEntity;
import com.techshare.entities.enums.MoveType;
import com.techshare.entities.enums.SaleStatus;
import com.techshare.exceptions.SaleNotFoundException;
import com.techshare.exceptions.UserNotFoundException;
import com.techshare.exceptions.InsufficientStockException;
import com.techshare.https.request.SaleRequest;
import com.techshare.https.request.SaleDetailRequest;
import com.techshare.https.request.MovementRequest;
import com.techshare.repositories.SaleRepository;
import com.techshare.repositories.UserRepository;
import com.techshare.repositories.MaterialRepository;
import com.techshare.services.sale.SaleService;
import com.techshare.services.movement.MovementService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ConvertSale convertSale;

    @Autowired
    private MovementService movementService;

    @Override
    @Transactional
    public SaleDTO createSale(SaleRequest saleRequest, Long userId) {
        // Get user
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Verificar stock disponible
        for (SaleDetailRequest detail : saleRequest.getDetails()) {
            Material material = materialRepository.findById(detail.getMaterial_id())
                    .orElseThrow(() -> new RuntimeException("Material not found with id: " + detail.getMaterial_id()));
            
            if (material.getStock() < detail.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for material: " + material.getName() + 
                    ". Available: " + material.getStock() + ", Requested: " + detail.getQuantity());
            }
        }

        // Crear la venta
        Sale sale = convertSale.convertSaleRequestToSale(saleRequest);
        sale.setUser(user);
        sale.setStatus(SaleStatus.COMPLETED);
        
        // Guardar la venta primero para tener el ID
        sale = saleRepository.save(sale);

        // Actualizar stock de materiales y crear registros de movimientos
        for (SaleDetailRequest detail : saleRequest.getDetails()) {
            Material material = materialRepository.findById(detail.getMaterial_id())
                    .orElseThrow(() -> new RuntimeException("Material not found with id: " + detail.getMaterial_id()));
            
            // Crear movimiento de tipo SALE
            MovementRequest movementRequest = new MovementRequest();
            movementRequest.setMaterial_id(material.getMaterial_id());
            movementRequest.setQuantity(detail.getQuantity());
            movementRequest.setMoveType(MoveType.SALE);
            movementRequest.setComment("Sale movement for sale ID: " + sale.getSale_id());
            movementService.createMovement(movementRequest);
        }
        
        return convertSale.convertSaleToSaleDTO(sale);
    }

    @Override
    public SaleDTO getSaleById(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with id: " + saleId));
        return convertSale.convertSaleToSaleDTO(sale);
    }

    @Override
    public List<SaleDTO> getAllSales() {
        return saleRepository.findAll().stream()
                .map(convertSale::convertSaleToSaleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SaleDTO> getSalesByUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return saleRepository.findByUser(user).stream()
                .map(convertSale::convertSaleToSaleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SaleDTO> getSalesByStatus(String status) {
        SaleStatus saleStatus = SaleStatus.valueOf(status.toUpperCase());
        return saleRepository.findByStatus(saleStatus).stream()
                .map(convertSale::convertSaleToSaleDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SaleDTO updateSaleStatus(Long saleId, String status) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with id: " + saleId));
        
        try {
            SaleStatus saleStatus = SaleStatus.valueOf(status.toUpperCase());
            sale.setStatus(saleStatus);
            sale = saleRepository.save(sale);
            return convertSale.convertSaleToSaleDTO(sale);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid sale status: " + status + ". Valid values are: PENDING, COMPLETED, CANCELLED");
        }
    }
}