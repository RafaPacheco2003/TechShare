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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {
    @Value("${storage.location}")
    private String storageLocation;


    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ConvertSubcategory convertSubcategory;

    @Autowired
    private ImageStorage imageStorage;

    @Override
    public SubcategoryDTO createSubcategory(SubcategoryRequest subcategoryRequest, MultipartFile multipartFile) {
        // Verificar si la categoría existe
        categoryService.verifyCategoryExists(subcategoryRequest.getCategory_id());

        // Guardar la imagen y obtener solo el nombre del archivo
        saveImage(subcategoryRequest, multipartFile);

        // Convertir la solicitud en entidad y guardar
        Subcategory subcategoryEntity = convertSubcategory.convertSubcategoryRequestToSubcategoryEntity(subcategoryRequest);
        Subcategory savedSubcategory = subcategoryRepository.save(subcategoryEntity);

        // Devolver DTO sin la URL completa de la imagen
        return convertSubcategory.convertSubcategoryEntityToSubcategoryDTO(savedSubcategory);
    }

    @Override
    public void saveImage(SubcategoryRequest subcategoryRequest, MultipartFile imageFile) {
        // Guardar la imagen utilizando el servicio de almacenamiento y obtener el nombre del archivo
        String imagePath = imageStorage.saveImage(imageFile);

        // Extraer solo el nombre del archivo de la ruta completa
        String imageName = Paths.get(imagePath).getFileName().toString();

        // Asignar el nombre de la imagen al subcategoryRequest
        subcategoryRequest.setImage(imageName);
    }




    @Override
    public Optional<SubcategoryDTO> getSubcategoryById(Long id) {
        return subcategoryRepository.findById(id)
                .map(subcategory -> {
                    // Convertir la entidad en DTO
                    SubcategoryDTO subcategoryDTO = convertSubcategory.convertSubcategoryEntityToSubcategoryDTO(subcategory);

                    // Crear la URL completa de la imagen
                    String imageUrl = "http://localhost:8080/images/" + subcategory.getImage();

                    // Establecer la URL de la imagen en el DTO
                    subcategoryDTO.setImage(imageUrl);

                    // Retornar el DTO con la URL de la imagen
                    return Optional.of(subcategoryDTO);
                })
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
                .map(subcategory -> {
                    // Convertir la entidad en DTO
                    SubcategoryDTO subcategoryDTO = convertSubcategory.convertSubcategoryEntityToSubcategoryDTO(subcategory);

                    // Crear la URL completa de la imagen
                    String imageUrl = "http://localhost:8080/images/" + subcategory.getImage();

                    // Establecer la URL de la imagen en el DTO
                    subcategoryDTO.setImage(imageUrl);

                    // Retornar el DTO con la URL de la imagen
                    return subcategoryDTO;
                })
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
    public List<SubcategoryDTO> getSubcategoriesByCategory(Long categoryId) {
        categoryService.verifyCategoryExists(categoryId);
        return subcategoryRepository.findByCategoryId(categoryId).stream()
                .map(subcategory -> {
                    SubcategoryDTO subcategoryDTO = convertSubcategory.convertSubcategoryEntityToSubcategoryDTO(subcategory);
                    String imageUrl = "http://localhost:8080/images/" + subcategory.getImage();
                    subcategoryDTO.setImage(imageUrl);
                    return subcategoryDTO;
                })
                .collect(Collectors.toList());
    }
}
