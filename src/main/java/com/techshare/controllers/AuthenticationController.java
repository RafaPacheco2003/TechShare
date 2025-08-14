package com.techshare.controllers;


import com.techshare.security.UserDetailServiceImpl;
import com.techshare.security.UserRegistrationService;
import com.techshare.entities.UserEntity;
import com.techshare.https.request.AuthLoginRequest;
import com.techshare.https.request.RegisterUserRequest;
import com.techshare.https.response.AuthResponse;
import com.techshare.services.accountVerification.AccountVerificationService;
import com.techshare.services.token.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserRegistrationService userRegistrationService;
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private AccountVerificationService accountVerificationService;
    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLoginRequest authLoginRequest, HttpServletResponse response) {
        AuthResponse authResponse = this.userDetailService.loginUser(authLoginRequest);
        if (authResponse.jwt() != null) {
            Cookie cookie = new Cookie("token", authResponse.jwt());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 30); // 30 minutos
            // cookie.setSecure(true); // Descomenta si usas HTTPS
            response.addCookie(cookie);
        }
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequest request) {
            userRegistrationService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente. Por favor verifica tu correo electrónico para activar tu cuenta.");
    }


    @PostMapping("/request-password")
    public ResponseEntity<?> requestPassword(@RequestParam String email){
    accountVerificationService.verificationAccount(email);

    return ResponseEntity.ok("Token enviado al correo electronico");
    }

    @GetMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(@RequestParam String token) {
        try {
            UserEntity user = tokenService.validateAndActivateAccount(token);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "¡Cuenta verificada exitosamente! Ahora puedes iniciar sesión.",
                    "username", user.getUsername(),
                    "redirectUrl", "/login" // URL a la que debe redirigir el frontend
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", e.getMessage(),
                    "solution", "Solicita un nuevo enlace de verificación"
            ));
        }
    }

}
