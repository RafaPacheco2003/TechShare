package com.techshare.http.response;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"username", "message", "jwt", "status"})
public record AuthResponse (String username, String message, String jwt, boolean status)

{
    @Override
    public String username() {
        return username;
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
