package com.techshare.exception;

public class MembershipNotFoundException extends RuntimeException {
    
    public MembershipNotFoundException(Long id) {
        super("Membership not found with ID: " + id);
    }
    
    public MembershipNotFoundException(String message) {
        super(message);
    }
    
    public MembershipNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 