package com.techshare.services.category;

import com.techshare.dto.CategoryDTO;
import com.techshare.http.request.CategoryRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CategoryDTO create(CategoryRequest categoryRequest, MultipartFile multipartFile);
    Optional<CategoryDTO> getCategoryById(Long id);
    Optional<CategoryDTO> updateCategory(Long id, CategoryRequest categoryRequest); // Actualizar una categoría por ID
    List<CategoryDTO> getAllCategories();
    void deleteCategory(Long id);

    void verifyCategoryExists(Long category_id);

    void saveImage(CategoryRequest categoryRequest, MultipartFile multipartFile);
}
