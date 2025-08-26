package com.techshare.controllers.user;

import com.techshare.https.response.ProductDTO;
import com.techshare.services.category.CategoryService;
import com.techshare.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeUserController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/category/all")
    public ResponseEntity<?> findAllCategories(){
        return new ResponseEntity<>(categoryService.getTop7Categories(), HttpStatus.OK);
    }

    @GetMapping("/product/all")
    public ResponseEntity<?> findAllMaterials() {
        return new ResponseEntity<>(productService.getAllMaterials(), HttpStatus.OK);
    }

    @GetMapping("/product/filter")
    public ResponseEntity<List<ProductDTO>> getMaterialsWithFilters(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long subcategoryId,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection) {
        List<ProductDTO> materials = productService.getMaterialsWithFilters(categoryId, subcategoryId, sortDirection);
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }

    @GetMapping("/product/highest-price")
    public ResponseEntity<ProductDTO> getMaterialWithHighestPrice() {
        ProductDTO material = productService.getMaterialWithHighestPrice();
        return material != null ?
                new ResponseEntity<>(material, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/product/lowest-price")
    public ResponseEntity<ProductDTO> getMaterialWithLowestPrice() {
        ProductDTO material = productService.getMaterialWithLowestPrice();
        return material != null ?
                new ResponseEntity<>(material, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/product/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getMaterialsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> materials = productService.getMaterialsByCategory(categoryId);
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }
}
