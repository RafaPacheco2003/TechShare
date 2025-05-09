package com.techshare.Security.validation;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationValidator {
    void validate(UserDetails userDetails, String rawPassword);
}
