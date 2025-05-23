package com.techshare.service.Sale;

import com.techshare.DTO.SaleDTO;
import com.techshare.http.request.SaleRequest;
import java.util.List;

public interface SaleService {
    SaleDTO createSale(SaleRequest saleRequest);
    SaleDTO getSaleById(Long saleId);
    List<SaleDTO> getAllSales();
    List<SaleDTO> getSalesByUser(Long userId);
    List<SaleDTO> getSalesByStatus(String status);
    SaleDTO updateSaleStatus(Long saleId, String status);
} 