package com.techshare.Security.validation.impl;


import com.techshare.Security.validation.AuthenticationValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator implements AuthenticationValidator {

    private final PasswordEncoder passwordEncoder;

    public PasswordValidator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void validate(UserDetails userDetails, String rawPassword) {
        if (!passwordEncoder.matches(rawPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }
    }
}