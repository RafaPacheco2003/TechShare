package com.techshare.services.sale;

import com.techshare.dto.SaleDTO;
import com.techshare.http.request.SaleRequest;
import java.util.List;

public interface SaleService {
    SaleDTO createSale(SaleRequest saleRequest, Long userId);
    SaleDTO getSaleById(Long saleId);
    List<SaleDTO> getAllSales();
    List<SaleDTO> getSalesByUser(Long userId);
    List<SaleDTO> getSalesByStatus(String status);
    SaleDTO updateSaleStatus(Long saleId, String status);
}