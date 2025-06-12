package com.techshare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(SaleController.class);

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SaleDTO> createSale(@Valid @RequestBody SaleRequest saleRequest) {
        logger.info("Creating new sale");
        return ResponseEntity.ok(saleService.createSale(saleRequest));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable Long id) {
        logger.info("Fetching sale with id: {}", id);
        return ResponseEntity.ok(saleService.getSaleById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SaleDTO>> getAllSales() {
        logger.info("Fetching all sales");
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<SaleDTO>> getSalesByUser(@PathVariable Long userId) {
        logger.info("Fetching sales for user: {}", userId);
        return ResponseEntity.ok(saleService.getSalesByUser(userId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SaleDTO>> getSalesByStatus(@PathVariable String status) {
        logger.info("Fetching sales with status: {}", status);
        return ResponseEntity.ok(saleService.getSalesByStatus(status));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SaleDTO> updateSaleStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        logger.info("Updating sale status. Sale ID: {}, New Status: {}", id, status);
        return ResponseEntity.ok(saleService.updateSaleStatus(id, status));
    }
}