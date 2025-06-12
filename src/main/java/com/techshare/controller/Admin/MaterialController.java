package com.techshare.controller.Admin;

import com.techshare.DTO.MaterialDTO;
import com.techshare.http.request.MaterialRequest;
import com.techshare.service.Material.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public ResponseEntity<?> create(@RequestPart("material") MaterialRequest materialRequest,
                                    @RequestPart("image")MultipartFile multipartFile) {
        MaterialDTO materialDTO = materialService.createMaterial(materialRequest, multipartFile);
        return new ResponseEntity<>(materialDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMaterialById(@PathVariable Long id) {
        return new ResponseEntity<>(materialService.getMaterialById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMaterial(@PathVariable Long id, @RequestBody MaterialRequest materialRequest) {
        return new ResponseEntity<>(materialService.updateMaterial(id, materialRequest), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllMaterials() {
        return new ResponseEntity<>(materialService.getAllMaterials(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return new ResponseEntity<>("Material ha sido eliminado", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MaterialDTO>> getMaterialsByCategory(@PathVariable Long categoryId) {
        List<MaterialDTO> materials = materialService.getMaterialsByCategory(categoryId);
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<MaterialDTO>> getMaterialsWithFilters(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long subcategoryId,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection) {
        List<MaterialDTO> materials = materialService.getMaterialsWithFilters(categoryId, subcategoryId, sortDirection);
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }

    @GetMapping("/highest-price")
    public ResponseEntity<MaterialDTO> getMaterialWithHighestPrice() {
        MaterialDTO material = materialService.getMaterialWithHighestPrice();
        return material != null ? 
            new ResponseEntity<>(material, HttpStatus.OK) : 
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/lowest-price")
    public ResponseEntity<MaterialDTO> getMaterialWithLowestPrice() {
        MaterialDTO material = materialService.getMaterialWithLowestPrice();
        return material != null ? 
            new ResponseEntity<>(material, HttpStatus.OK) : 
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
