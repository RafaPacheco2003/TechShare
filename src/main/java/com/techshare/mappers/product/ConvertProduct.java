package com.techshare.mappers.product;

import com.techshare.https.response.ProductDTO;
import com.techshare.entities.Material;
import com.techshare.https.request.ProductRequest;

public interface ConvertProduct {
    Material convertMaterialRequestToMaterial(ProductRequest productRequest);

    void convertUpdateMaterialRequestToMaterial(ProductRequest productRequest, Material existingMaterial);

    ProductDTO convertMaterialToMaterialDTO(Material material);
}