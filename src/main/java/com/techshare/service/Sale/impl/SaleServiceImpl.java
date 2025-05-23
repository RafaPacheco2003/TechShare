package com.techshare.service.Sale.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techshare.DTO.SaleDTO;
import com.techshare.convert.Sale.ConvertSale;
import com.techshare.entities.Sale;
import com.techshare.entities.Material;
import com.techshare.entities.UserEntity;
import com.techshare.entities.enums.SaleStatus;
import com.techshare.exception.SaleNotFoundException;
import com.techshare.exception.UserNotFoundException;
import com.techshare.exception.InsufficientStockException;
import com.techshare.http.request.SaleRequest;
import com.techshare.http.request.SaleDetailRequest;
import com.techshare.repositories.SaleRepository;
import com.techshare.repositories.UserRepository;
import com.techshare.repositories.MaterialRepository;
import com.techshare.service.Sale.SaleService;

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

    @Override
    @Transactional
    public SaleDTO createSale(SaleRequest saleRequest) {
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
        sale.setStatus(SaleStatus.COMPLETED);
        
        // Actualizar stock de materiales
        for (SaleDetailRequest detail : saleRequest.getDetails()) {
            Material material = materialRepository.findById(detail.getMaterial_id())
                    .orElseThrow(() -> new RuntimeException("Material not found with id: " + detail.getMaterial_id()));
            
            // Actualizar el stock
            material.setStock(material.getStock() - detail.getQuantity());
            materialRepository.save(material);
        }
        
        // Guardar la venta
        sale = saleRepository.save(sale);
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