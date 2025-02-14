package com.techshare.service.Category.impl;

import com.techshare.DTO.CategoryDTO;
import com.techshare.convert.Category.ConvertCategory;
import com.techshare.entities.Category;
import com.techshare.exception.CategoryNotFoundException;
import com.techshare.http.request.CategoryRequest;
import com.techshare.repositories.CategoryRepository;
import com.techshare.service.Category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ConvertCategory convertCategory;

    @Override
    public CategoryDTO create(CategoryRequest categoryRequest) {
        Category categoryEntity = convertCategory.convertCategoryRequestToCategory(categoryRequest);
        Category savedCategory = categoryRepository.save(categoryEntity);
        return convertCategory.convertCategoryToCategoryDTO(savedCategory);
    }

    @Override
    public Optional<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> Optional.of(convertCategory.convertCategoryToCategoryDTO(category)))  // Convierte a DTO si se encuentra
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
                .map(convertCategory::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Category not found with ID: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
