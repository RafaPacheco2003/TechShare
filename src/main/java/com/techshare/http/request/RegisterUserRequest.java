package com.techshare.http.request;


import lombok.Data;

import java.util.Set;


public class RegisterUserRequest {
    private String username;
    private String password;
    private Set<String> roles; // Nombres de los roles (por ejemplo, "ADMIN", "USER")

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}