package com.techshare.controllers.dev;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.techshare.util.JwtDecoderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/decode")
public class JwtDecoderController {

    @Autowired
    private JwtDecoderService jwtDecoderService;



    @GetMapping("/user/id")
    public ResponseEntity<?> getUserId(@RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("=== /api/decode/user/id called ===");
        System.out.println("Authorization header: " + authorizationHeader);

        try {
            Long userId = jwtDecoderService.extractUserIdFromHeader(authorizationHeader);
            System.out.println("Success! User ID: " + userId);
            return ResponseEntity.ok(userId);

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage(),
                    "timestamp", new Date()
            ));
        }
    }
    /**
     * Endpoint para decodificar y obtener información del token
     */
    @GetMapping("/decode")
    public ResponseEntity<?> decodeToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            DecodedJWT decodedJWT = jwtDecoderService.decodeTokenFromHeader(authorizationHeader);
            String username = jwtDecoderService.getUsernameFromHeader(authorizationHeader);
            List<String> authorities = jwtDecoderService.getAuthoritiesFromHeader(authorizationHeader);

            return ResponseEntity.ok(Map.of(
                    "username", username,
                    "authorities", authorities,
                    "issuedAt", decodedJWT.getIssuedAt(),
                    "expiresAt", decodedJWT.getExpiresAt(),
                    "issuer", decodedJWT.getIssuer()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para verificar si el token es válido
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        boolean isValid = jwtDecoderService.isTokenValid(authorizationHeader);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }

    /**
     * Endpoint para obtener el username del token
     */
    @GetMapping("/username")
    public ResponseEntity<?> getUsername(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String username = jwtDecoderService.getUsernameFromHeader(authorizationHeader);
            return ResponseEntity.ok(Map.of("username", username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
