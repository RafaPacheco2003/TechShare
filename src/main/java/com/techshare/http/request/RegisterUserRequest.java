package com.techshare.http.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;


public class RegisterUserRequest {




    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Debe ser un email válido (ejemplo: usuario@dominio.com)")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String username;

        @NotBlank(message = "Password no puede estar vacío")
        @Size(min = 6, message = "Password debe tener al menos 6 caracteres")
        private String password;

        @NotBlank(message = "First name no puede estar vacío")
        private String firstName;

        @NotBlank(message = "Last name no puede estar vacío")
        private String lastName;

        @NotNull(message = "Roles no puede ser nulo")
        @Size(min = 1, message = "Debe asignar al menos un rol")
        private Set<String> roles;


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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}