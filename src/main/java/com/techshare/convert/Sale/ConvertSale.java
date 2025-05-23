package com.techshare.convert.Sale;

import com.techshare.DTO.SaleDTO;
import com.techshare.entities.Sale;
import com.techshare.http.request.SaleRequest;

public interface ConvertSale {
    Sale convertSaleRequestToSale(SaleRequest saleRequest);
    SaleDTO convertSaleToSaleDTO(Sale sale);
} 