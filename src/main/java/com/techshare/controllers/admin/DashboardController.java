package com.techshare.controllers.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getDashboardMessage() {
        return ResponseEntity.ok("Bienvenido al Dashboard. Acceso restringido solo para administradores.");
    }
} 