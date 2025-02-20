package com.techshare.controller.Admin;

import com.techshare.DTO.SubcategoryDTO;
import com.techshare.http.request.SubcategoryRequest;
import com.techshare.service.Subcategory.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/subcategory")
public class SubcategoryController {

    @Autowired
    private SubcategoryService subcategoryService;

    @PostMapping
    public ResponseEntity<?> create(@RequestPart("subcategory") SubcategoryRequest subcategoryRequest,
                                    @RequestPart("image") MultipartFile imageFile) {
        String imageUrl = subcategoryService.saveImage(imageFile);
        subcategoryRequest.setImage(imageUrl); // Guardar la URL de la imagen
        return new ResponseEntity<>(subcategoryService.createSubcategory(subcategoryRequest), HttpStatus.CREATED);
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
}
