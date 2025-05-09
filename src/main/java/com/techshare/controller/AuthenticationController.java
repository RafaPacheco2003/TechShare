package com.techshare.controller;


import com.techshare.Security.UserDetailServiceImpl;
import com.techshare.Security.UserRegistrationService;
import com.techshare.entities.UserEntity;
import com.techshare.http.request.AuthLoginRequest;
import com.techshare.http.request.RegisterUserRequest;
import com.techshare.http.response.AuthResponse;
import com.techshare.service.AccountVerification.AccountVerificationService;
import com.techshare.service.Token.TokenService;
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
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLoginRequest authLoginRequest) {
        return new ResponseEntity<>(this.userDetailService.loginUser(authLoginRequest), HttpStatus.OK);
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
