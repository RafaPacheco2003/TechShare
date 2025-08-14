package com.techshare.services.accountVerification.impl;

import com.techshare.entities.UserEntity;
import com.techshare.repositories.UserRepository;
import com.techshare.services.email.EmailService;
import com.techshare.services.accountVerification.AccountVerificationService;
import com.techshare.services.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountVerificationServiceImpl implements AccountVerificationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private EmailService emailService;

    @Override
    public void verificationAccount(String email) {
        try {
            UserEntity user = userRepository.findUserEntityByUsername(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            // Verificar si la cuenta ya está activada
            if (user.isEnabled()) {
                throw new RuntimeException("La cuenta ya está verificada");
            }

            String token = tokenService.generateToken(user);
            emailService.sendVerificationRegister(email, token);

        } catch (RuntimeException e) {
            throw e; // Re-lanzar estas excepciones específicas
        } catch (Exception e) {
            throw new RuntimeException("Error en el proceso de verificación. Recuerda que el enlace es válido solo por 5 minutos.", e);
        }
    }
}
