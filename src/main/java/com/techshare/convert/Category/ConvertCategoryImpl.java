package com.techshare.convert.Category;

import com.techshare.DTO.CategoryDTO;
import com.techshare.entities.Category;
import com.techshare.http.request.CategoryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConvertCategoryImpl implements  ConvertCategory{

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Category convertCategoryRequestToCategory(CategoryRequest categoryRequest) {
        return modelMapper.map(categoryRequest, Category.class);
    }

    @Override
    public void convertUpdateCategoryRequestToCategory(CategoryRequest categoryRequest, Category existingCategory) {
        modelMapper.map(categoryRequest, existingCategory);
    }

    @Override
    public CategoryDTO convertCategoryToCategoryDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }
}
