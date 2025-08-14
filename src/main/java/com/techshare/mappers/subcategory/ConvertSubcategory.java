package com.techshare.mappers.subcategory;

import com.techshare.dto.SubcategoryDTO;
import com.techshare.entities.Subcategory;
import com.techshare.http.request.SubcategoryRequest;

public interface ConvertSubcategory {
    Subcategory convertSubcategoryRequestToSubcategoryEntity(SubcategoryRequest subcategoryRequest);
    void convertUpdateSubcategoryRequestToSubcategory(SubcategoryRequest subcategoryRequest, Subcategory existingSubcategory);
    SubcategoryDTO convertSubcategoryEntityToSubcategoryDTO(Subcategory subcategory);

}
