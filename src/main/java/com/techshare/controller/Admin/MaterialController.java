package com.techshare.controller.Admin;

import com.techshare.DTO.MaterialDTO;
import com.techshare.http.request.MaterialRequest;
import com.techshare.service.Material.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MaterialRequest materialRequest) {
        MaterialDTO materialDTO = materialService.createMaterial(materialRequest);
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
}
