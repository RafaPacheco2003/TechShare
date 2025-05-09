package com.techshare.service.RecoverPassword.impl;

import com.techshare.entities.UserEntity;
import com.techshare.repositories.UserRepository;
import com.techshare.service.Email.EmailService;
import com.techshare.service.RecoverPassword.RecoverPasswordService;
import com.techshare.service.Token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RecoveryPasswordServiceImpl implements RecoverPasswordService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private EmailService emailService;

    @Override
    public void recoverPassword(String email) {

        UserEntity userEntity= userRepository.findUserEntityByUsername(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        String token = tokenService.generateToken(userEntity);

    }
}
