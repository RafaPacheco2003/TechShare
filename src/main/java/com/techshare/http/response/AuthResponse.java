package com.techshare.http.response;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"username", "firstName", "userId", "role", "message", "jwt", "status"})
public record AuthResponse (
    String username, 
    String firstName,
    Long userId,
    String role,
    String message, 
    String jwt, 
    boolean status
) {
    @Override
    public String username() {
        return username;
    }

    @Override
    public String firstName() {
        return firstName;
    }

    @Override
    public Long userId() {
        return userId;
    }

    @Override
    public String role() {
        return role;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String jwt() {
        return jwt;
    }

    @Override
    public boolean status() {
        return status;
    }
}
