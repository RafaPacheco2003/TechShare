package com.techshare.service.Category.impl;

import com.techshare.DTO.CategoryDTO;
import com.techshare.convert.Category.ConvertCategory;
import com.techshare.entities.Category;
import com.techshare.exception.CategoryNotFoundException;
import com.techshare.http.request.CategoryRequest;
import com.techshare.http.request.SubcategoryRequest;
import com.techshare.repositories.CategoryRepository;
import com.techshare.service.Category.CategoryService;
import com.techshare.service.ImageStorage.ImageStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageStorage imageStorage;

    @Autowired
    private ConvertCategory convertCategory;

    @Override
    public CategoryDTO create(CategoryRequest categoryRequest, MultipartFile multipartFile) {

        // Guardar la imagen y obtener el nombre
        saveImage(categoryRequest, multipartFile);
        // Convertir la solicitud en una entidad
        Category categoryEntity = convertCategory.convertCategoryRequestToCategory(categoryRequest);

        // Guardar la categoría
        Category savedCategory = categoryRepository.save(categoryEntity);

        // Convertir la entidad guardada en DTO y devolverla
        return convertCategory.convertCategoryToCategoryDTO(savedCategory);
    }


    @Override
    public Optional<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    CategoryDTO categoryDTO= convertCategory.convertCategoryToCategoryDTO(category);
                    String imageUrl=  "http://localhost:8080/images/" + category.getImage();
                    categoryDTO.setImage(imageUrl);
                    return Optional.of(categoryDTO);
                })
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));  // Lanza excepción si no
    }

    @Override
    public Optional<CategoryDTO> updateCategory(Long id, CategoryRequest categoryRequest) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    convertCategory.convertUpdateCategoryRequestToCategory(categoryRequest, existingCategory);
                    Category updatedCategory = categoryRepository.save(existingCategory);  // Guarda la categoría actualizada
                    return Optional.of(convertCategory.convertCategoryToCategoryDTO(updatedCategory));  // Convierte a DTO y devuelve el resultado
                })
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));  // Lanza excepción si no se encuentra
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> {
                    CategoryDTO categoryDTO= convertCategory.convertCategoryToCategoryDTO(category);
                    // Crear la URL completa de la imagen
                    String imageUrl = "http://localhost:8080/images/" + category.getImage();

                    // Establecer la URL de la imagen en el DTO
                    categoryDTO.setImage(imageUrl);

                    // Retornar el DTO con la URL de la imagen
                    return categoryDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Category not found with ID: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public void verifyCategoryExists(Long category_id) {
        if (!categoryRepository.existsById(category_id)) {
            throw new CategoryNotFoundException("Category not found");
        }
    }

    @Override
    public void saveImage(CategoryRequest categoryRequest, MultipartFile multipartFile) {
        // Guardar la imagen utilizando el servicio de almacenamiento y obtener el nombre del archivo
        String imagePath = imageStorage.saveImage(multipartFile);

        // Extraer solo el nombre del archivo de la ruta completa
        String imageName = Paths.get(imagePath).getFileName().toString();

        // Asignar el nombre de la imagen al subcategoryRequest
        categoryRequest.setImage(imageName);
    }


}
