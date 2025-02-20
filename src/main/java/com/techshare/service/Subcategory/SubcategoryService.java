package com.techshare.service.Subcategory;

import com.techshare.DTO.SubcategoryDTO;
import com.techshare.http.request.SubcategoryRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface SubcategoryService {
    SubcategoryDTO createSubcategory(SubcategoryRequest subcategoryRequest);
    Optional<SubcategoryDTO> getSubcategoryById(Long id);
    Optional<SubcategoryDTO> updateSubcategory(Long id, SubcategoryRequest subcategoryRequest);
    List<SubcategoryDTO> getAllSubcategories();
    void deleteSubcategory(Long id);

    String saveImage(MultipartFile imageFile);
}
