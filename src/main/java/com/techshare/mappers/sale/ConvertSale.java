package com.techshare.mappers.sale;

import com.techshare.dto.SaleDTO;
import com.techshare.entities.Sale;
import com.techshare.http.request.SaleRequest;
 
public interface ConvertSale {
    Sale convertSaleRequestToSale(SaleRequest saleRequest);
    SaleDTO convertSaleToSaleDTO(Sale sale);
}