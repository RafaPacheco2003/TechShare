package com.techshare.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MainPasswordEncrypter {

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Contraseña en texto plano
        String plainPassword = "123";

        // Encriptar la contraseña
        String encryptedPassword = passwordEncoder.encode(plainPassword);

        // Mostrar la contraseña encriptada
        System.out.println("Contraseña encriptada: " + encryptedPassword);

        // Verificar si la contraseña en texto plano coincide con la encriptada
        boolean isMatch = passwordEncoder.matches(plainPassword, encryptedPassword);

        System.out.println("¿La contraseña coincide?: " + isMatch);
    }
}