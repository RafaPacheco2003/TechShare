package com.techshare.mappers.subcategory;

import com.techshare.https.response.SubcategoryDTO;
import com.techshare.entities.Category;
import com.techshare.entities.Subcategory;
import com.techshare.https.request.SubcategoryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ConvertSubcategoryImpl implements ConvertSubcategory{

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Subcategory convertSubcategoryRequestToSubcategoryEntity(SubcategoryRequest subcategoryRequest) {
        Subcategory subcategory = new Subcategory();
        subcategory.setName(subcategoryRequest.getName());
        subcategory.setImage(subcategoryRequest.getImage());

        // Asignar categoría a partir del category_id
        Category category = new Category();
        category.setCategory_id(subcategoryRequest.getCategory_id());
        subcategory.setCategory(category);

        return subcategory;
    }

    @Override
    public void convertUpdateSubcategoryRequestToSubcategory(SubcategoryRequest subcategoryRequest, Subcategory existingSubcategory) {
        existingSubcategory.setName(subcategoryRequest.getName());
        existingSubcategory.setImage(subcategoryRequest.getImage());

        // Si es necesario actualizar la categoría
        if (subcategoryRequest.getCategory_id() != null) {
            Category category = new Category();
            category.setCategory_id(subcategoryRequest.getCategory_id());
            existingSubcategory.setCategory(category);
        }
    }


    @Override
    public SubcategoryDTO convertSubcategoryEntityToSubcategoryDTO(Subcategory subcategory) {
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO();
        subcategoryDTO.setSubcategory_id(subcategory.getSubcategory_id());
        subcategoryDTO.setName(subcategory.getName());
        subcategoryDTO.setImage(subcategory.getImage());

        if (subcategory.getCategory() != null) {
            subcategoryDTO.setCategory_id(subcategory.getCategory().getCategory_id());
        }

        return subcategoryDTO;
    }

}
