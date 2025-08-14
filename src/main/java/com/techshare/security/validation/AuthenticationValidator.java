package com.techshare.security.validation;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationValidator {
    void validate(UserDetails userDetails, String rawPassword);
}
