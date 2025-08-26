package com.techshare.mappers.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.techshare.https.response.ProductDTO;
import com.techshare.entities.Material;
import com.techshare.https.request.ProductRequest;
import com.techshare.repositories.UserRepository;
import com.techshare.repositories.SubcategoryRepository;

@Component
public class ConvertProductImpl implements ConvertProduct {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;
    
    @Override    public Material convertMaterialRequestToMaterial(ProductRequest productRequest) {
        Material material = new Material();
        material.setName(productRequest.getName());
        material.setDescripcion(productRequest.getDescription());
        material.setStock(productRequest.getStock());
        material.setPrice(productRequest.getPrice());
        material.setImage(productRequest.getImage());
        
        if (productRequest.getSubcategory_id() != null) {
            material.setSubcategory(subcategoryRepository.findById(productRequest.getSubcategory_id())
                    .orElseThrow(() -> new RuntimeException("Subcategory not found")));
        }
        
        if (productRequest.getUser_id() != null) {
            material.setUser(userRepository.findById(productRequest.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found")));
        }
        
        return material;
    }    @Override
    public void convertUpdateMaterialRequestToMaterial(ProductRequest productRequest, Material existingMaterial) {
        existingMaterial.setName(productRequest.getName());
        existingMaterial.setDescripcion(productRequest.getDescription());
        existingMaterial.setStock(productRequest.getStock());
        existingMaterial.setPrice(productRequest.getPrice());
        existingMaterial.setImage(productRequest.getImage());
        
        if (productRequest.getSubcategory_id() != null) {
            existingMaterial.setSubcategory(subcategoryRepository.findById(productRequest.getSubcategory_id())
                    .orElseThrow(() -> new RuntimeException("Subcategory not found")));
        }
        
        if (productRequest.getUser_id() != null) {
            existingMaterial.setUser(userRepository.findById(productRequest.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found")));
        }
    }@Override
    public ProductDTO convertMaterialToMaterialDTO(Material material) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProduct_id(material.getProduct_id());
        productDTO.setName(material.getName());
        productDTO.setDescription(material.getDescripcion());
        productDTO.setStock(material.getStock());
        productDTO.setPrice(material.getPrice());
        productDTO.setImage(material.getImage());
        
        if (material.getSubcategory() != null) {
            productDTO.setSubcategory_id(material.getSubcategory().getSubcategory_id());
            productDTO.setSubcategory_name(material.getSubcategory().getName());
        }
        
        if (material.getUser() != null) {
            productDTO.setUser_id(material.getUser().getUser_id());
        }
        
        return productDTO;
    }
}