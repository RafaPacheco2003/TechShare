package com.techshare.convert.Material;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.techshare.DTO.MaterialDTO;
import com.techshare.entities.Material;
import com.techshare.http.request.MaterialRequest;
import com.techshare.repositories.UserRepository;

@Component
public class ConvertMaterialImpl implements ConvertMaterial {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Material convertMaterialRequestToMaterial(MaterialRequest materialRequest) {
        Material material = new Material();
        material.setName(materialRequest.getName());
        material.setDescripcion(materialRequest.getDescription());
        material.setStock(materialRequest.getStock());
        material.setPrice(materialRequest.getPrice());
        material.setImage(materialRequest.getImage());
        
        if (materialRequest.getUser_id() != null) {
            material.setUser(userRepository.findById(materialRequest.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found")));
        }
        
        return material;
    }

    @Override
    public void convertUpdateMaterialRequestToMaterial(MaterialRequest materialRequest, Material existingMaterial) {
        existingMaterial.setName(materialRequest.getName());
        existingMaterial.setDescripcion(materialRequest.getDescription());
        existingMaterial.setStock(materialRequest.getStock());
        existingMaterial.setPrice(materialRequest.getPrice());
        existingMaterial.setImage(materialRequest.getImage());
        
        if (materialRequest.getUser_id() != null) {
            existingMaterial.setUser(userRepository.findById(materialRequest.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found")));
        }
    }

    @Override
    public MaterialDTO convertMaterialToMaterialDTO(Material material) {
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMaterial_id(material.getMaterial_id());
        materialDTO.setName(material.getName());
        materialDTO.setDescription(material.getDescripcion());
        materialDTO.setStock(material.getStock());
        materialDTO.setPrice(material.getPrice());
        materialDTO.setImage(material.getImage());
        
        if (material.getUser() != null) {
            materialDTO.setUser_id(material.getUser().getUser_id());
        }
        
        return materialDTO;
    }
}