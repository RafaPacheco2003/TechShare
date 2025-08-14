package com.techshare.mappers.sale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techshare.https.response.SaleDTO;
import com.techshare.https.response.SaleDetailDTO;
import com.techshare.entities.Sale;
import com.techshare.entities.SaleDetail;
import com.techshare.entities.Material;
import com.techshare.entities.enums.SaleStatus;
import com.techshare.https.request.SaleRequest;
import com.techshare.https.request.SaleDetailRequest;
import com.techshare.repositories.MaterialRepository;
import com.techshare.exceptions.MaterialNotFoundException;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConvertSaleImpl implements ConvertSale {

    @Autowired
    private MaterialRepository materialRepository;

    @Override
    public Sale convertSaleRequestToSale(SaleRequest saleRequest) {
        Sale sale = new Sale();
        sale.setSaleDate(new Date());
        sale.setStatus(SaleStatus.PENDING);
        
        Set<SaleDetail> details = new HashSet<>();
        Double totalAmount = 0.0;

        for (SaleDetailRequest detailRequest : saleRequest.getDetails()) {
            Material material = materialRepository.findById(detailRequest.getMaterial_id())
                    .orElseThrow(() -> new MaterialNotFoundException("Material not found with id: " + detailRequest.getMaterial_id()));

            SaleDetail detail = new SaleDetail();
            detail.setSale(sale);
            detail.setMaterial(material);
            detail.setQuantity(detailRequest.getQuantity());
            detail.setUnitPrice(material.getPrice());
            detail.setSubtotal(material.getPrice() * detailRequest.getQuantity());
            
            details.add(detail);
            totalAmount += detail.getSubtotal();
        }

        sale.setSaleDetails(details);
        sale.setTotalAmount(totalAmount);

        return sale;
    }

    @Override
    public SaleDTO convertSaleToSaleDTO(Sale sale) {
        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setSale_id(sale.getSale_id());
        saleDTO.setUser_id(sale.getUser().getUser_id());
        saleDTO.setUserName(sale.getUser().getUsername());
        saleDTO.setSaleDate(sale.getSaleDate());
        saleDTO.setTotalAmount(sale.getTotalAmount());
        saleDTO.setStatus(sale.getStatus());

        Set<SaleDetailDTO> detailDTOs = sale.getSaleDetails().stream()
                .map(detail -> {
                    SaleDetailDTO detailDTO = new SaleDetailDTO();
                    detailDTO.setSaleDetail_id(detail.getSaleDetail_id());
                    detailDTO.setMaterial_id(detail.getMaterial().getMaterial_id());
                    detailDTO.setMaterialName(detail.getMaterial().getName());
                    detailDTO.setQuantity(detail.getQuantity());
                    detailDTO.setUnitPrice(detail.getUnitPrice());
                    detailDTO.setSubtotal(detail.getSubtotal());
                    return detailDTO;
                })
                .collect(Collectors.toSet());

        saleDTO.setSaleDetails(detailDTOs);
        return saleDTO;
    }
}