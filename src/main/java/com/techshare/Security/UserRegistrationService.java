package com.techshare.Security;

import com.techshare.entities.RoleEntity;
import com.techshare.entities.RoleEnum;
import com.techshare.entities.UserEntity;
import com.techshare.http.request.RegisterUserRequest;
import com.techshare.repositories.RoleRepository;
import com.techshare.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserRegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity registerUser(RegisterUserRequest request) {
        // Verificar si el usuario ya existe
        if (userRepository.findUserEntityByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        // Crear un nuevo usuario
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setAccountNoExpired(true);
        user.setCredentialNoExpired(true);
        user.setAccountNoLocked(true);

        // Asignar roles al usuario
        Set<RoleEntity> roles = new HashSet<>();
        for (String roleName : request.getRoles()) {
            RoleEntity role = roleRepository.findByRoleEnum(RoleEnum.valueOf(roleName))
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName));
            roles.add(role);
        }
        user.setRoles(roles);

        // Guardar el usuario en la base de datos
        return userRepository.save(user);
    }
}