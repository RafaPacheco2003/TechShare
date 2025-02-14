package com.techshare.service.Category;

import com.techshare.DTO.CategoryDTO;
import com.techshare.http.request.CategoryRequest;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CategoryDTO create(CategoryRequest categoryRequest);
    Optional<CategoryDTO> getCategoryById(Long id);
    Optional<CategoryDTO> updateCategory(Long id, CategoryRequest categoryRequest); // Actualizar una categoría por ID
    List<CategoryDTO> getAllCategories(); // Obtener todas las categorías
    void deleteCategory(Long id); // Eliminar una categoría por ID
}
