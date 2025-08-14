package com.techshare.mappers.material;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.techshare.dto.MaterialDTO;
import com.techshare.entities.Material;
import com.techshare.http.request.MaterialRequest;
import com.techshare.repositories.UserRepository;
import com.techshare.repositories.SubcategoryRepository;

@Component
public class ConvertMaterialImpl implements ConvertMaterial {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;
    
    @Override    public Material convertMaterialRequestToMaterial(MaterialRequest materialRequest) {
        Material material = new Material();
        material.setName(materialRequest.getName());
        material.setDescripcion(materialRequest.getDescription());
        material.setStock(materialRequest.getStock());
        material.setPrice(materialRequest.getPrice());
        material.setImage(materialRequest.getImage());
        
        if (materialRequest.getSubcategory_id() != null) {
            material.setSubcategory(subcategoryRepository.findById(materialRequest.getSubcategory_id())
                    .orElseThrow(() -> new RuntimeException("Subcategory not found")));
        }
        
        if (materialRequest.getUser_id() != null) {
            material.setUser(userRepository.findById(materialRequest.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found")));
        }
        
        return material;
    }    @Override
    public void convertUpdateMaterialRequestToMaterial(MaterialRequest materialRequest, Material existingMaterial) {
        existingMaterial.setName(materialRequest.getName());
        existingMaterial.setDescripcion(materialRequest.getDescription());
        existingMaterial.setStock(materialRequest.getStock());
        existingMaterial.setPrice(materialRequest.getPrice());
        existingMaterial.setImage(materialRequest.getImage());
        
        if (materialRequest.getSubcategory_id() != null) {
            existingMaterial.setSubcategory(subcategoryRepository.findById(materialRequest.getSubcategory_id())
                    .orElseThrow(() -> new RuntimeException("Subcategory not found")));
        }
        
        if (materialRequest.getUser_id() != null) {
            existingMaterial.setUser(userRepository.findById(materialRequest.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found")));
        }
    }@Override
    public MaterialDTO convertMaterialToMaterialDTO(Material material) {
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMaterial_id(material.getMaterial_id());
        materialDTO.setName(material.getName());
        materialDTO.setDescription(material.getDescripcion());
        materialDTO.setStock(material.getStock());
        materialDTO.setPrice(material.getPrice());
        materialDTO.setImage(material.getImage());
        
        if (material.getSubcategory() != null) {
            materialDTO.setSubcategory_id(material.getSubcategory().getSubcategory_id());
            materialDTO.setSubcategory_name(material.getSubcategory().getName());
        }
        
        if (material.getUser() != null) {
            materialDTO.setUser_id(material.getUser().getUser_id());
        }
        
        return materialDTO;
    }
}