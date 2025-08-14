package com.techshare.controllers.admin;

import com.techshare.http.request.CategoryRequest;
import com.techshare.services.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public ResponseEntity<?> create(@RequestPart("category") CategoryRequest categoryRequest,
                                    @RequestPart("image") MultipartFile multipartFile) {
        return new ResponseEntity<>(categoryService.create(categoryRequest, multipartFile), HttpStatus.CREATED);
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
