package com.techshare.controller;

import com.techshare.DTO.CategoryDTO;
import com.techshare.http.request.CategoryRequest;
import com.techshare.service.Category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryRequest categoryRequest) {
        return new ResponseEntity<>(categoryService.create(categoryRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        return  new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest){
        return  new ResponseEntity<>(categoryService.updateCategory(id, categoryRequest), HttpStatus.OK);
    }

    @GetMapping("/all")
    public  ResponseEntity<?> findAllCategories(){
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Categoria a sido eliminada",HttpStatus.NO_CONTENT);
    }
}
