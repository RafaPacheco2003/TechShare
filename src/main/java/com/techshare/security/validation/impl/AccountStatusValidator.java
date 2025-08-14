package com.techshare.security.validation.impl;


import com.techshare.security.validation.AuthenticationValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Component;

@Component
public class AccountStatusValidator implements AuthenticationValidator {

    @Override
    public void validate(UserDetails userDetails, String rawPassword) {
        if (!userDetails.isEnabled()) {
            throw new DisabledException("La cuenta no está activada. Verifica tu email");
        }
    }
}