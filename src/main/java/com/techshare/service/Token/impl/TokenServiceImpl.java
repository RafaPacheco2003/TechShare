package com.techshare.service.Token.impl;

import com.techshare.entities.UserEntity;
import com.techshare.entities.VerificationToken;
import com.techshare.repositories.UserRepository;
import com.techshare.repositories.VerificationTokenRepository;
import com.techshare.service.Token.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;

    public TokenServiceImpl(VerificationTokenRepository tokenRepository,
                            UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public String generateToken(UserEntity user) {

        tokenRepository.deleteByUser(user);


        VerificationToken token = new VerificationToken(user);
        tokenRepository.save(token);

        return token.getToken();
    }

    @Override
    @Transactional
    public UserEntity validateAndActivateAccount(String tokenValue) {
        VerificationToken token = tokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new RuntimeException("Enlace de verificación inválido"));

        if (token.isExpired()) {
            throw new RuntimeException("El enlace de verificación ha expirado");
        }

        UserEntity user = token.getUser();
        if (user.isEnabled()) {
            throw new RuntimeException("La cuenta ya está verificada");
        }

        // Activar la cuenta
        user.setEnabled(true);
        userRepository.save(user);

        // Opcional: Eliminar el token ya usado
        tokenRepository.delete(token);

        return user;
    }
}