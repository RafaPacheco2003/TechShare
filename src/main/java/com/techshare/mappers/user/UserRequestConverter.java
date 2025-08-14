package com.techshare.mappers.user;

import com.techshare.entities.UserEntity;
import com.techshare.https.request.RegisterUserRequest;

/**
 * Interfaz para convertir RegisterUserRequest a UserEntity
 */
public interface UserRequestConverter {
    
    /**
     * Convierte un objeto RegisterUserRequest a un objeto UserEntity
     * @param request El objeto RegisterUserRequest a convertir
     * @return Un objeto UserEntity con los datos del request
     */
    UserEntity convert(RegisterUserRequest request);
} 