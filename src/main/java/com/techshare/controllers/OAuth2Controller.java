package com.techshare.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {
    
    private static final Logger logger = LoggerFactory.getLogger(OAuth2Controller.class);

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @GetMapping("/login/google")
    public void redirectToGoogleLogin(HttpServletResponse response) throws IOException {
        logger.info("Iniciando proceso de login con Google");
        //d
        // Redirigir al endpoint de autorización de Spring Security OAuth2
        String authorizationUrl = "/oauth2/authorization/google";
        logger.info("Redirigiendo a: {}", authorizationUrl);
        response.sendRedirect(authorizationUrl);
    }

    @GetMapping("/success")
    public ResponseEntity<Map<String, String>> success(@RequestParam(required = false) String token) {
        logger.info("Autenticación exitosa, token recibido: {}", token != null);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Autenticación exitosa!");
        if (token != null) {
            response.put("token", token);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/error")
    public ResponseEntity<Map<String, String>> error(@RequestParam(required = false) String message) {
        logger.error("Error en autenticación OAuth2: {}", message);
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("error_description", message != null ? message : "Error desconocido en la autenticación");
        return ResponseEntity.badRequest().body(response);
    }
} 