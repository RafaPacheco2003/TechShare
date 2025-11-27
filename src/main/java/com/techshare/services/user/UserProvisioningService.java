package com.techshare.services.user;

import com.techshare.entities.RoleEntity;
import com.techshare.entities.UserEntity;
import com.techshare.entities.enums.RoleEnum;
import com.techshare.repositories.RoleRepository;
import com.techshare.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Servicio para gestionar el aprovisionamiento automático de usuarios.
 * Se encarga de buscar o crear usuarios que vienen de sistemas externos como OAuth2.
 */
@Service
public class UserProvisioningService {

    private static final Logger logger = LoggerFactory.getLogger(UserProvisioningService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserProvisioningService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Busca un usuario por username (email). Si no existe, lo crea automáticamente
     * con rol USER y datos básicos derivados del email.
     *
     * @param username El email/username del usuario
     * @return El usuario encontrado o creado
     */
    @Transactional
    public UserEntity findOrCreateUser(String username) {
        return userRepository.findUserEntityByUsername(username)
                .orElseGet(() -> createBasicUser(username, null, null));
    }

    /**
     * Busca un usuario por username (email). Si no existe, lo crea automáticamente
     * con rol USER y los datos proporcionados.
     *
     * @param username El email/username del usuario
     * @param firstName Nombre del usuario (opcional)
     * @param lastName Apellido del usuario (opcional)
     * @return El usuario encontrado o creado
     */
    @Transactional
    public UserEntity findOrCreateUser(String username, String firstName, String lastName) {
        return userRepository.findUserEntityByUsername(username)
                .orElseGet(() -> createBasicUser(username, firstName, lastName));
    }

    /**
     * Crea un nuevo usuario con configuración básica y rol USER.
     * Este método es privado y solo debe ser llamado internamente.
     *
     * @param username El email/username del usuario
     * @param firstName Nombre del usuario (opcional, se deriva del email si es null)
     * @param lastName Apellido del usuario (opcional)
     * @return El usuario recién creado
     */
    private UserEntity createBasicUser(String username, String firstName, String lastName) {
        logger.info("Creating new user from external authentication: {}", username);

        // Obtener rol USER
        RoleEntity userRole = roleRepository.findByRoleEnum(RoleEnum.USER)
                .orElseThrow(() -> new IllegalStateException("Role USER not found in database. Database may not be properly initialized."));

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);

        // Crear nuevo usuario
        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // Password aleatorio seguro
        
        // Configurar nombre: usar el proporcionado o derivar del email
        if (firstName != null && !firstName.isEmpty()) {
            newUser.setFirstName(firstName);
        } else {
            // Extraer la parte antes del @ del email como nombre
            String derivedName = username.contains("@") ? username.split("@")[0] : username;
            newUser.setFirstName(derivedName);
        }
        
        newUser.setLastName(lastName != null ? lastName : "");
        
        // Configurar permisos de cuenta
        newUser.setEnabled(true);
        newUser.setAccountNoExpired(true);
        newUser.setCredentialNoExpired(true);
        newUser.setAccountNoLocked(true);
        newUser.setRoles(roles);

        UserEntity savedUser = userRepository.save(newUser);
        logger.info("New user created successfully with ID: {} and role: USER", savedUser.getUser_id());

        return savedUser;
    }

    /**
     * Verifica si un usuario existe en la base de datos.
     *
     * @param username El email/username del usuario
     * @return true si el usuario existe, false en caso contrario
     */
    public boolean userExists(String username) {
        return userRepository.findUserEntityByUsername(username).isPresent();
    }

    /**
     * Busca un usuario por username sin crearlo si no existe.
     *
     * @param username El email/username del usuario
     * @return Optional con el usuario si existe, Optional.empty() si no existe
     */
    public Optional<UserEntity> findUser(String username) {
        return userRepository.findUserEntityByUsername(username);
    }
}
