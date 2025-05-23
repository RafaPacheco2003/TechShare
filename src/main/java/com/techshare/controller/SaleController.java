package com.techshare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techshare.DTO.SaleDTO;
import com.techshare.http.request.SaleRequest;
import com.techshare.service.Sale.SaleService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@Valid @RequestBody SaleRequest saleRequest) {
        return ResponseEntity.ok(saleService.createSale(saleRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getSaleById(id));
    }

    @GetMapping
    public ResponseEntity<List<SaleDTO>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SaleDTO>> getSalesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(saleService.getSalesByUser(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SaleDTO>> getSalesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(saleService.getSalesByStatus(status));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SaleDTO> updateSaleStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(saleService.updateSaleStatus(id, status));
    }
} 