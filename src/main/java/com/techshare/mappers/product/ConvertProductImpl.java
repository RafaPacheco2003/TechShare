package com.techshare.mappers.product;

import com.techshare.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.techshare.https.response.ProductDTO;
import com.techshare.https.request.ProductRequest;
import com.techshare.repositories.UserRepository;
import com.techshare.repositories.SubcategoryRepository;

@Component
public class ConvertProductImpl implements ConvertProduct {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;
    
    @Override    public Product convertMaterialRequestToMaterial(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescripcion(productRequest.getDescription());
        product.setStock(productRequest.getStock());
        product.setPrice(productRequest.getPrice());
        product.setImage(productRequest.getImage());
        
        if (productRequest.getSubcategory_id() != null) {
            product.setSubcategory(subcategoryRepository.findById(productRequest.getSubcategory_id())
                    .orElseThrow(() -> new RuntimeException("Subcategory not found")));
        }
        
        if (productRequest.getUser_id() != null) {
            product.setUser(userRepository.findById(productRequest.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found")));
        }
        
        return product;
    }    @Override
    public void convertUpdateMaterialRequestToMaterial(ProductRequest productRequest, Product existingProduct) {
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescripcion(productRequest.getDescription());
        existingProduct.setStock(productRequest.getStock());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setImage(productRequest.getImage());
        
        if (productRequest.getSubcategory_id() != null) {
            existingProduct.setSubcategory(subcategoryRepository.findById(productRequest.getSubcategory_id())
                    .orElseThrow(() -> new RuntimeException("Subcategory not found")));
        }
        
        if (productRequest.getUser_id() != null) {
            existingProduct.setUser(userRepository.findById(productRequest.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found")));
        }
    }@Override
    public ProductDTO convertMaterialToMaterialDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProduct_id(product.getProduct_id());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescripcion());
        productDTO.setStock(product.getStock());
        productDTO.setPrice(product.getPrice());
        productDTO.setImage(product.getImage());
        
        if (product.getSubcategory() != null) {
            productDTO.setSubcategory_id(product.getSubcategory().getSubcategory_id());
            productDTO.setSubcategory_name(product.getSubcategory().getName());
        }
        
        if (product.getUser() != null) {
            productDTO.setUser_id(product.getUser().getUser_id());
            productDTO.setUser_name(product.getUser().getFirstName() + " " + product.getUser().getLastName());
        }
        
        return productDTO;
    }
}