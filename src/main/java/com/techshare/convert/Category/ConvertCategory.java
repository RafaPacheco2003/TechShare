package com.techshare.convert.Category;

import com.techshare.DTO.CategoryDTO;
import com.techshare.entities.Category;
import com.techshare.http.request.CategoryRequest;

public interface ConvertCategory {
    Category convertCategoryRequestToCategory(CategoryRequest categoryRequest);
    void convertUpdateCategoryRequestToCategory(CategoryRequest categoryRequest, Category existingCategory);
    CategoryDTO convertCategoryToCategoryDTO(Category category);
}
