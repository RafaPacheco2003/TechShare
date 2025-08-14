package com.techshare.security.validation.impl;

import com.techshare.security.validation.AuthenticationValidator;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserExistenceValidator implements AuthenticationValidator {
    @Override
    public void validate(UserDetails userDetails, String rawPassword) {
        if (userDetails == null){
            throw new BadCredentialsException("Usuario no encontrado");
        }
    }
}
