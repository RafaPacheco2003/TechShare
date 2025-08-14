package com.techshare.mappers.sale;

import com.techshare.https.response.SaleDTO;
import com.techshare.entities.Sale;
import com.techshare.https.request.SaleRequest;
 
public interface ConvertSale {
    Sale convertSaleRequestToSale(SaleRequest saleRequest);
    SaleDTO convertSaleToSaleDTO(Sale sale);
}