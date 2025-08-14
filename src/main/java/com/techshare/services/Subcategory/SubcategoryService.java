package com.techshare.services.Subcategory;

import com.techshare.dto.SubcategoryDTO;
import com.techshare.http.request.SubcategoryRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface SubcategoryService {
    SubcategoryDTO createSubcategory(SubcategoryRequest subcategoryRequest, MultipartFile multipartFile);
    Optional<SubcategoryDTO> getSubcategoryById(Long id);
    Optional<SubcategoryDTO> updateSubcategory(Long id, SubcategoryRequest subcategoryRequest);
    List<SubcategoryDTO> getAllSubcategories();
    void deleteSubcategory(Long id);
    void saveImage(SubcategoryRequest subcategoryRequest, MultipartFile imageFile);
    List<SubcategoryDTO> getSubcategoriesByCategory(Long categoryId);
}
