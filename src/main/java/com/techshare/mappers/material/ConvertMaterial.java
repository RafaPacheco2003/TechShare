package com.techshare.mappers.material;

import com.techshare.dto.MaterialDTO;
import com.techshare.entities.Material;
import com.techshare.http.request.MaterialRequest;

public interface ConvertMaterial {
    Material convertMaterialRequestToMaterial(MaterialRequest materialRequest);

    void convertUpdateMaterialRequestToMaterial(MaterialRequest materialRequest, Material existingMaterial);

    MaterialDTO convertMaterialToMaterialDTO(Material material);
}