package com.techshare.services.product;

import com.techshare.https.response.ProductDTO;
import com.techshare.https.request.ProductRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductDTO createMaterial(ProductRequest productRequest, MultipartFile multipartFile);  // Añadir MultipartFile para la imagen
    Optional<ProductDTO> getMaterialById(Long id);
    Optional<ProductDTO> updateMaterial(Long id, ProductRequest productRequest);
    List<ProductDTO> getAllMaterials();
    void deleteMaterial(Long id);
    void saveImage(ProductRequest productRequest, MultipartFile multipartFile);  // Método para guardar imagen
    List<ProductDTO> getMaterialsByCategory(Long categoryId);  // New method
    List<ProductDTO> getMaterialsWithFilters(Long categoryId, Long subcategoryId, String sortDirection);
    ProductDTO getMaterialWithHighestPrice();
    ProductDTO getMaterialWithLowestPrice();
}
