package com.techshare.service.Token;

import com.techshare.entities.UserEntity;

public interface TokenService {

    String generateToken(UserEntity userEntity);
    UserEntity validateAndActivateAccount(String tokenValue);
}
