package com.techshare.security;

import com.techshare.mappers.user.UserRequestConverter;
import com.techshare.entities.UserEntity;
import com.techshare.https.request.RegisterUserRequest;
import com.techshare.repositories.UserRepository;
import com.techshare.services.accountVerification.AccountVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  UserRequestConverter userRequestConverter;
    @Autowired
    private  AccountVerificationService accountVerificationService;


    @Transactional
    public UserEntity registerUser(RegisterUserRequest request) {
        // Verificar usuario existente
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El usuario ya existe");
        }

        // Convertir y guardar usuario
        UserEntity user = userRequestConverter.convert(request);
        UserEntity savedUser = userRepository.save(user);

        try {
            accountVerificationService.verificationAccount(savedUser.getUsername());
            return savedUser;
        } catch (Exception e) {

            throw new RuntimeException("Usuario registrado, pero no se pudo enviar el correo de verificación. Contacta al administrador.");

            // Opción 2: Para desarrollo, puedes devolver el usuario igual
            // return savedUser;
        }
    }
}