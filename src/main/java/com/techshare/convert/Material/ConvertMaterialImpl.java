package com.techshare.convert.Material;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techshare.DTO.MaterialDTO;
import com.techshare.entities.Material;
import com.techshare.entities.Membership;
import com.techshare.http.request.MaterialRequest;
import com.techshare.repositories.MembershipRepository;
import com.techshare.repositories.SubcategoryRepository;

@Service
public class ConvertMaterialImpl implements ConvertMaterial {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Override
    public Material convertMaterialRequestToMaterial(MaterialRequest materialRequest) {
        Material material = new Material();
        material.setName(materialRequest.getName());
        material.setDescripcion(materialRequest.getDescripcion());
        material.setStock(materialRequest.getStock());
        material.setPrice(materialRequest.getPrice());
        material.setImage(materialRequest.getImage());
        
        if (materialRequest.getSubcategory() != null) {
            material.setSubcategory(subcategoryRepository.findById(materialRequest.getSubcategory())
                    .orElse(null));
        }

        if (materialRequest.getMembership_id() != null) {
            material.setMembership(membershipRepository.findById(materialRequest.getMembership_id())
                    .orElse(null));
        }

        return material;
    }

    @Override
    public void convertUpdateMaterialRequestToMaterial(MaterialRequest materialRequest, Material existingMaterial) {
        existingMaterial.setName(materialRequest.getName());
        existingMaterial.setDescripcion(materialRequest.getDescripcion());
        existingMaterial.setStock(materialRequest.getStock());
        existingMaterial.setPrice(materialRequest.getPrice());
        existingMaterial.setImage(materialRequest.getImage());
        
        if (materialRequest.getSubcategory() != null) {
            existingMaterial.setSubcategory(subcategoryRepository.findById(materialRequest.getSubcategory())
                    .orElse(null));
        }

        if (materialRequest.getMembership_id() != null) {
            existingMaterial.setMembership(membershipRepository.findById(materialRequest.getMembership_id())
                    .orElse(null));
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
        
        if (material.getSubcategory() != null) {
            materialDTO.setSubcategory(material.getSubcategory().getSubcategory_id());
        }

        if (material.getMembership() != null) {
            materialDTO.setMembership_id(material.getMembership().getMembership_id());
        }

        return materialDTO;
    }
}