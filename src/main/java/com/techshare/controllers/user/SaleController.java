package com.techshare.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techshare.https.response.SaleDTO;
import com.techshare.https.request.SaleRequest;
import com.techshare.services.sale.SaleService;
import com.techshare.services.user.UserProvisioningService;
import com.techshare.entities.UserEntity;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para gestionar las ventas.
 * Maneja la creación, consulta y actualización de ventas en el sistema.
 */
@RestController
@RequestMapping("/api/sale")
public class SaleController {

    private static final Logger logger = LoggerFactory.getLogger(SaleController.class);

    private final SaleService saleService;
    private final UserProvisioningService userProvisioningService;

    @Autowired
    public SaleController(SaleService saleService, UserProvisioningService userProvisioningService) {
        this.saleService = saleService;
        this.userProvisioningService = userProvisioningService;
    }

    /**
     * Obtiene el usuario autenticado del contexto de seguridad.
     * Si el usuario no existe en la base de datos, lo crea automáticamente.
     * Esto es útil para usuarios que vienen de OAuth2.
     */
    private UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userProvisioningService.findOrCreateUser(username);
    }

    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@Valid @RequestBody SaleRequest saleRequest) {
        logger.info("Creating new sale");
        UserEntity user = getCurrentUser();
        logger.debug("Sale being created by user ID: {}", user.getUser_id());
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
        logger.info("Fetching sales by authenticated user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logger.debug("Fetching sales for username: {}", username);
        
        // Buscar o crear usuario si no existe
        UserEntity user = userProvisioningService.findOrCreateUser(username);
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