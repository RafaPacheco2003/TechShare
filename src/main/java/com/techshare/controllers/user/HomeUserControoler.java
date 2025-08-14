package com.techshare.controllers.user;

import com.techshare.dto.MaterialDTO;
import com.techshare.services.category.CategoryService;
import com.techshare.services.material.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeUserControoler {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MaterialService materialService;

    @GetMapping("/category/all")
    public ResponseEntity<?> findAllCategories(){
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @GetMapping("/material/all")
    public ResponseEntity<?> findAllMaterials() {
        return new ResponseEntity<>(materialService.getAllMaterials(), HttpStatus.OK);
    }

    @GetMapping("/material/filter")
    public ResponseEntity<List<MaterialDTO>> getMaterialsWithFilters(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long subcategoryId,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection) {
        List<MaterialDTO> materials = materialService.getMaterialsWithFilters(categoryId, subcategoryId, sortDirection);
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }

    @GetMapping("/material/highest-price")
    public ResponseEntity<MaterialDTO> getMaterialWithHighestPrice() {
        MaterialDTO material = materialService.getMaterialWithHighestPrice();
        return material != null ?
                new ResponseEntity<>(material, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/material/lowest-price")
    public ResponseEntity<MaterialDTO> getMaterialWithLowestPrice() {
        MaterialDTO material = materialService.getMaterialWithLowestPrice();
        return material != null ?
                new ResponseEntity<>(material, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/material/category/{categoryId}")
    public ResponseEntity<List<MaterialDTO>> getMaterialsByCategory(@PathVariable Long categoryId) {
        List<MaterialDTO> materials = materialService.getMaterialsByCategory(categoryId);
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }
}
