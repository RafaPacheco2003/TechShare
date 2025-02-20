package com.techshare.service.Subcategory.impl;

import com.techshare.DTO.SubcategoryDTO;
import com.techshare.convert.Subcategory.ConvertSubcategory;
import com.techshare.entities.Subcategory;
import com.techshare.http.request.SubcategoryRequest;
import com.techshare.repositories.CategoryRepository;
import com.techshare.repositories.SubcategoryRepository;
import com.techshare.service.Category.CategoryService;
import com.techshare.service.ImageStorage.ImageStorage;
import com.techshare.service.Subcategory.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ConvertSubcategory convertSubcategory;

    @Value("${server.url")
    private String server;
    @Autowired
    private ImageStorage imageStorage;

    @Override
    public SubcategoryDTO createSubcategory(SubcategoryRequest subcategoryRequest) {

        categoryService.verifyCategoryExists(subcategoryRequest.getCategory_id());
        Subcategory subcategoryEntity = convertSubcategory.convertSubcategoryRequestToSubcategoryEntity(subcategoryRequest);
        Subcategory savedSubcategory = subcategoryRepository.save(subcategoryEntity);

        return convertSubcategory.convertSubcategoryEntityToSubcategoryDTO(savedSubcategory);
    }

    @Override
    public Optional<SubcategoryDTO> getSubcategoryById(Long id) {
        return subcategoryRepository.findById(id)
                .map(subcategory -> Optional.of(convertSubcategory.convertSubcategoryEntityToSubcategoryDTO(subcategory)))
                .orElseThrow(() -> new RuntimeException("Subcategory not found"));
    }

    @Override
    public Optional<SubcategoryDTO> updateSubcategory(Long id, SubcategoryRequest subcategoryRequest) {
        return subcategoryRepository.findById(id)
                .map(existingSubcategory -> {
                    convertSubcategory.convertUpdateSubcategoryRequestToSubcategory(subcategoryRequest, existingSubcategory);
                    Subcategory updateSubcategory = subcategoryRepository.save(existingSubcategory);
                    return Optional.of(convertSubcategory.convertSubcategoryEntityToSubcategoryDTO(updateSubcategory));
                })
                .orElseThrow(() -> new RuntimeException("Subactegory not found"));
    }

    @Override
    public List<SubcategoryDTO> getAllSubcategories() {
        return subcategoryRepository.findAll().stream()
                .map(convertSubcategory::convertSubcategoryEntityToSubcategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSubcategory(Long id) {
        if (!subcategoryRepository.existsById(id)){
            throw new RuntimeException("Subcategory not found");
        }
        subcategoryRepository.deleteById(id);
    }

    @Override
    public String saveImage(MultipartFile imageFile) {
        return imageStorage.saveImage(imageFile); // Delegar el guardado de la imagen al servicio de almacenamiento
    }
}
