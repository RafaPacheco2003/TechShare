package com.techshare.util;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.techshare.entities.UserEntity;
import com.techshare.repositories.UserRepository;
import com.techshare.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JwtDecoderService {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;


    /**
     * Extrae el EMAIL del token y luego busca el ID en la base de datos
     */
    public Long extractUserIdFromHeader(String authorizationHeader) {
        System.out.println("Authorization header received: " + authorizationHeader);

        if (!StringUtils.hasText(authorizationHeader)) {
            throw new IllegalArgumentException("Authorization header cannot be null or empty");
        }

        try {
            // 1. Limpiar el token
            String token = authorizationHeader.replace("Bearer ", "").trim();

            // 2. Validar y decodificar el token
            var decodedJWT = jwtUtils.validateToken(token);

            // 3. Extraer el EMAIL del subject
            String userEmail = jwtUtils.extractUsername(decodedJWT);
            System.out.println("Email extracted from token: " + userEmail);

            // 4. Buscar el usuario por email en la base de datos
            UserEntity user = userRepository.findByUsername(userEmail)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + userEmail));

            // 5. Retornar el ID del usuario
            System.out.println("User found with ID: " + user.getUser_id());
            return user.getUser_id();

        } catch (Exception e) {
            System.out.println("ERROR in extractUserIdFromHeader: " + e.getMessage());
            throw new RuntimeException("Error processing token: " + e.getMessage(), e);
        }
    }
    /**
     * Decodifica y valida un token JWT desde el header
     */
    public DecodedJWT decodeTokenFromHeader(String authorizationHeader) {
        if (!StringUtils.hasText(authorizationHeader)) {
            throw new IllegalArgumentException("Authorization header cannot be null or empty");
        }

        // Limpiar el token (remover "Bearer " si existe)
        String token = authorizationHeader.replace("Bearer ", "").trim();

        return jwtUtils.validateToken(token);
    }

    /**
     * Obtiene el username desde el header de autorización
     */
    public String getUsernameFromHeader(String authorizationHeader) {
        DecodedJWT decodedJWT = decodeTokenFromHeader(authorizationHeader);
        return jwtUtils.extractUsername(decodedJWT);
    }

    /**
     * Obtiene los authorities/roles desde el header
     */
    public List<String> getAuthoritiesFromHeader(String authorizationHeader) {
        DecodedJWT decodedJWT = decodeTokenFromHeader(authorizationHeader);
        String authorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();

        if (authorities == null || authorities.isEmpty()) {
            return List.of();
        }

        return Stream.of(authorities.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    /**
     * Verifica si el token es válido
     */
    public boolean isTokenValid(String authorizationHeader) {
        try {
            decodeTokenFromHeader(authorizationHeader);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene un claim específico desde el header
     */
    public String getClaimFromHeader(String authorizationHeader, String claimName) {
        DecodedJWT decodedJWT = decodeTokenFromHeader(authorizationHeader);
        return jwtUtils.getSpecificClaim(decodedJWT, claimName).asString();
    }


}