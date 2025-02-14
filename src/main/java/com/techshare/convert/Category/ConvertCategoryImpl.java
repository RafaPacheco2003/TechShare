package com.techshare.convert.Category;

import com.techshare.DTO.CategoryDTO;
import com.techshare.entities.Category;
import com.techshare.http.request.CategoryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConvertCategoryImpl implements ConvertCategory {

    @Override
    public Category convertCategoryRequestToCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setImage(categoryRequest.getImage());
        return category;
    }

    @Override
    public void convertUpdateCategoryRequestToCategory(CategoryRequest categoryRequest, Category existingCategory) {
        existingCategory.setName(categoryRequest.getName());
        existingCategory.setImage(categoryRequest.getImage());
    }

    @Override
    public CategoryDTO convertCategoryToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategory_id(category.getCategory_id());
        categoryDTO.setName(category.getName());
        categoryDTO.setImage(category.getImage());
        return categoryDTO;
    }
}
