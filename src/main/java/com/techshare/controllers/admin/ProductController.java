package com.techshare.controllers.admin;

import com.techshare.https.response.ProductDTO;
import com.techshare.https.request.ProductRequest;
import com.techshare.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public ResponseEntity<?> create(@RequestPart("material") ProductRequest productRequest,
                                    @RequestPart("image")MultipartFile multipartFile) {
        ProductDTO productDTO = productService.createMaterial(productRequest, multipartFile);
        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMaterialById(@PathVariable Long id) {
        return new ResponseEntity<>(productService.getMaterialById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMaterial(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(productService.updateMaterial(id, productRequest), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllMaterials() {
        return new ResponseEntity<>(productService.getAllMaterials(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long id) {
        productService.deleteMaterial(id);
        return new ResponseEntity<>("Material ha sido eliminado", HttpStatus.NO_CONTENT);
    }




}
