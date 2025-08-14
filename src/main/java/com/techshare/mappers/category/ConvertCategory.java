package com.techshare.mappers.category;

import com.techshare.https.response.CategoryDTO;
import com.techshare.entities.Category;
import com.techshare.https.request.CategoryRequest;

public interface ConvertCategory {
    Category convertCategoryRequestToCategory(CategoryRequest categoryRequest);
    void convertUpdateCategoryRequestToCategory(CategoryRequest categoryRequest, Category existingCategory);
    CategoryDTO convertCategoryToCategoryDTO(Category category);
}
