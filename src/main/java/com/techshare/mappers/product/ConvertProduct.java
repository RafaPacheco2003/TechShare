package com.techshare.mappers.product;

import com.techshare.entities.Product;
import com.techshare.https.response.ProductDTO;
import com.techshare.https.request.ProductRequest;

public interface ConvertProduct {
    Product convertMaterialRequestToMaterial(ProductRequest productRequest);

    void convertUpdateMaterialRequestToMaterial(ProductRequest productRequest, Product existingProduct);

    ProductDTO convertMaterialToMaterialDTO(Product product);
}