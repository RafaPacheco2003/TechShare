package com.techshare.mappers.user;

import com.techshare.entities.RoleEntity;
import com.techshare.entities.UserEntity;
import com.techshare.http.request.RegisterUserRequest;
import com.techshare.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Implementación del conversor de RegisterUserRequest a UserEntity
 */
@Component
public class UserRequestConverterImpl implements UserRequestConverter {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Convierte un objeto RegisterUserRequest a un objeto UserEntity
     * @param request El objeto RegisterUserRequest a convertir
     * @return Un objeto UserEntity con los datos del request
     */
    @Override
    public UserEntity convert(RegisterUserRequest request) {
        // Validar y obtener roles
        Set<RoleEntity> roles = roleService.validateAndGetRoles(request.getRoles());
        
        // Crear un nuevo usuario
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEnabled(false); // Usuario no está habilitado hasta que verifique su correo
        user.setAccountNoExpired(true);
        user.setCredentialNoExpired(true);
        user.setAccountNoLocked(true);
        user.setRoles(roles);
        
        return user;
    }
} 