package com.techshare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techshare.DTO.SaleDTO;
import com.techshare.http.request.SaleRequest;
import com.techshare.service.Sale.SaleService;
import com.techshare.repositories.UserRepository;
import com.techshare.entities.UserEntity;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(SaleController.class);

    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@Valid @RequestBody SaleRequest saleRequest) {
        logger.info("Creating new sale");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findUserEntityByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(saleService.createSale(saleRequest, user.getUser_id()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable Long id) {
        logger.info("Fetching sale with id: {}", id);
        return ResponseEntity.ok(saleService.getSaleById(id));
    }

    @GetMapping
    public ResponseEntity<List<SaleDTO>> getAllSales() {
        logger.info("Fetching all sales");
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @GetMapping("/user")
    public ResponseEntity<List<SaleDTO>> getSalesByUser() {

        logger.info("Creating new sale");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        logger.info("Fetching sales for user: {}", user.getUser_id());
        return ResponseEntity.ok(saleService.getSalesByUser(user.getUser_id()));
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