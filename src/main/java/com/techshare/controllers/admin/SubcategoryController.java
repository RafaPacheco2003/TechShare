package com.techshare.controllers.admin;

import com.techshare.dto.SubcategoryDTO;
import com.techshare.http.request.SubcategoryRequest;
import com.techshare.services.Subcategory.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/subcategory")
public class SubcategoryController {


    @Autowired
    private SubcategoryService subcategoryService;

    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public ResponseEntity<?> create(@RequestPart("subcategory") SubcategoryRequest subcategoryRequest,
                                    @RequestPart("image") MultipartFile multipartFile) {
        // Crear la subcategoría y obtener el DTO
        SubcategoryDTO subcategoryDTO = subcategoryService.createSubcategory(subcategoryRequest, multipartFile);

        // Retornar solo el DTO sin la URL completa de la imagen
        return new ResponseEntity<>(subcategoryDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSubcategoryById(@PathVariable Long id) {
        return new ResponseEntity<>(subcategoryService.getSubcategoryById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubcategory(@PathVariable Long id, @RequestBody SubcategoryRequest subcategoryRequest) {
        return new ResponseEntity<>(subcategoryService.updateSubcategory(id, subcategoryRequest), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllSubcategories() {
        return new ResponseEntity<>(subcategoryService.getAllSubcategories(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubcategory(@PathVariable Long id) {
        subcategoryService.deleteSubcategory(id);
        return new ResponseEntity<>("Subcategoría ha sido eliminada", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<SubcategoryDTO>> getSubcategoriesByCategoryId(@PathVariable Long categoryId) {
        List<SubcategoryDTO> subcategories = subcategoryService.getSubcategoriesByCategory(categoryId);
        return new ResponseEntity<>(subcategories, HttpStatus.OK);
    }
}
