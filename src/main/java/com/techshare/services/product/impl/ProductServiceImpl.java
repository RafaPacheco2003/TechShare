package com.techshare.services.product.impl;

import com.techshare.https.response.ProductDTO;
import com.techshare.mappers.product.ConvertProduct;
import com.techshare.entities.Material;
import com.techshare.entities.Subcategory;
import com.techshare.https.request.ProductRequest;
import com.techshare.repositories.ProductRepository;
import com.techshare.repositories.SubcategoryRepository;
import com.techshare.services.category.CategoryService;
import com.techshare.services.imageStorage.ImageStorage;
import com.techshare.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageStorage imageStorage;  // Autowired del servicio de imágenes

    @Autowired
    private ConvertProduct convertMaterial;

    @Override
    public ProductDTO createMaterial(ProductRequest productRequest, MultipartFile multipartFile) {
        // Guardar la imagen y obtener el nombre
        saveImage(productRequest, multipartFile);

        // Convertir la solicitud en entidad
        Material materialEntity = convertMaterial.convertMaterialRequestToMaterial(productRequest);

        // Guardar el material en la base de datos
        Material savedMaterial = productRepository.save(materialEntity);

        // Convertir la entidad guardada en DTO y devolverla
        return convertMaterial.convertMaterialToMaterialDTO(savedMaterial);
    }

    @Override
    public Optional<ProductDTO> getMaterialById(Long id) {
        return productRepository.findById(id)
                .map(material -> {
                    ProductDTO productDTO = convertMaterial.convertMaterialToMaterialDTO(material);
                    // Crear URL de imagen y establecerla en el DTO
                    String imageUrl = "http://localhost:8080/images/" + material.getImage();
                    productDTO.setImage(imageUrl);
                    return Optional.of(productDTO);
                })
                .orElseThrow(() -> new RuntimeException("Material not found with ID: " + id));  // Excepción personalizada
    }

    @Override
    public Optional<ProductDTO> updateMaterial(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(existingMaterial -> {
                    convertMaterial.convertUpdateMaterialRequestToMaterial(productRequest, existingMaterial);
                    Material updatedMaterial = productRepository.save(existingMaterial);  // Guardar el material actualizado
                    return Optional.of(convertMaterial.convertMaterialToMaterialDTO(updatedMaterial));  // Devolver el DTO actualizado
                })
                .orElseThrow(() -> new RuntimeException("Material not found with ID: " + id));
    }

    @Override
    public List<ProductDTO> getAllMaterials() {
        return productRepository.findAll().stream()
                .map(material -> {
                    ProductDTO productDTO = convertMaterial.convertMaterialToMaterialDTO(material);
                    // Crear URL de la imagen
                    String imageUrl = "http://localhost:8080/images/" + material.getImage();
                    productDTO.setImage(imageUrl);
                    return productDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMaterial(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Material not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public void saveImage(ProductRequest productRequest, MultipartFile multipartFile) {
        // Guardar la imagen usando el servicio de almacenamiento y obtener la ruta
        String imagePath = imageStorage.saveImage(multipartFile);

        // Extraer solo el nombre del archivo de la ruta completa
        String imageName = Paths.get(imagePath).getFileName().toString();

        // Asignar el nombre de la imagen al materialRequest
        productRequest.setImage(imageName);
    }

    @Override
    public List<ProductDTO> getMaterialsByCategory(Long categoryId) {
        // Verify if category exists, this will throw CategoryNotFoundException if not found
        categoryService.verifyCategoryExists(categoryId);
        
        // Get all subcategories for the given category
        List<Subcategory> subcategories = subcategoryRepository.findByCategoryId(categoryId);
        
        // Extract subcategory IDs
        List<Long> subcategoryIds = subcategories.stream()
                .map(Subcategory::getSubcategory_id)
                .collect(Collectors.toList());
        
        // If no subcategories found, return empty list
        if (subcategoryIds.isEmpty()) {
            return List.of();
        }
        
        // Get all materials for these subcategories
        return productRepository.findBySubcategoryIds(subcategoryIds).stream()
                .map(material -> {
                    ProductDTO productDTO = convertMaterial.convertMaterialToMaterialDTO(material);
                    // Crear URL de la imagen
                    String imageUrl = "http://localhost:8080/images/" + material.getImage();
                    productDTO.setImage(imageUrl);
                    return productDTO;
                })
                .collect(Collectors.toList());
    }   @Override
public List<ProductDTO> getMaterialsWithFilters(Long categoryId, Long subcategoryId, String sortDirection) {
    Sort sort = Sort.by(
        sortDirection != null && sortDirection.equalsIgnoreCase("asc")
            ? Sort.Direction.ASC
            : Sort.Direction.DESC,
        "price"
    );

    List<Material> materials;
    // Si no hay filtros, usar findAllMaterials
    if (categoryId == null && subcategoryId == null) {
        materials = productRepository.findAllMaterials(sort);
    } else {
        materials = productRepository.findMaterialsWithFilters(categoryId, subcategoryId, sort);
    }
    
    return materials.stream()
            .map(material -> {
                ProductDTO productDTO = convertMaterial.convertMaterialToMaterialDTO(material);
                // Crear URL de la imagen
                String imageUrl = "http://localhost:8080/images/" + material.getImage();
                productDTO.setImage(imageUrl);
                return productDTO;
            })
            .collect(Collectors.toList());
}


    @Override
    public ProductDTO getMaterialWithHighestPrice() {
        Material material = productRepository.findTopByOrderByPriceDesc();
        return material != null ? convertMaterial.convertMaterialToMaterialDTO(material) : null;
    }

    @Override
    public ProductDTO getMaterialWithLowestPrice() {
        Material material = productRepository.findTopByOrderByPriceAsc();
        return material != null ? convertMaterial.convertMaterialToMaterialDTO(material) : null;
    }
}
