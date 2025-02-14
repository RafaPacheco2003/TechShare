package com.techshare.convert.Subcategory;

import com.techshare.DTO.SubcategoryDTO;
import com.techshare.entities.Category;
import com.techshare.entities.Subcategory;
import com.techshare.http.request.CategoryRequest;
import com.techshare.http.request.SubcategoryRequest;

public interface ConvertSubcategory {
    Subcategory convertSubcategoryRequestToSubcategoryEntity(SubcategoryRequest subcategoryRequest);
    void convertUpdateSubcategoryRequestToSubcategory(SubcategoryRequest subcategoryRequest, Subcategory existingSubcategory);
    SubcategoryDTO convertSubcategoryEntityToSubcategoryDTO(Subcategory subcategory);

}
