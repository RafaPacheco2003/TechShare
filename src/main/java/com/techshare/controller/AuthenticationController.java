package com.techshare.controller;


import com.techshare.Security.UserDetailServiceImpl;
import com.techshare.Security.UserRegistrationService;
import com.techshare.http.request.AuthLoginRequest;
import com.techshare.http.request.RegisterUserRequest;
import com.techshare.http.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserRegistrationService userRegistrationService;
    @Autowired
    private UserDetailServiceImpl userDetailService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLoginRequest authLoginRequest) {
        return new ResponseEntity<>(this.userDetailService.loginUser(authLoginRequest), HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequest request) {
        try {
            userRegistrationService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
