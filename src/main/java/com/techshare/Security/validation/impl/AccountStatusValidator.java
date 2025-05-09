package com.techshare.Security.validation.impl;


import com.techshare.Security.validation.AuthenticationValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.DisabledException;
import org.springframework.core.annotation.Order;
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