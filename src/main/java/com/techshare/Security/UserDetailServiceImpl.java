package com.techshare.Security;

import com.techshare.entities.UserEntity;
import com.techshare.http.request.AuthLoginRequest;
import com.techshare.http.response.AuthResponse;
import com.techshare.repositories.UserRepository;
import com.techshare.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;
@Service
public class UserDetailServiceImpl implements UserDetailsService {



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Asegúrate de inyectar el PasswordEncoder

    @Autowired
    private JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Buscando usuario: {}", username); // Log para verificar que se está buscando el usuario correcto

        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> {
                    logger.error("El usuario {} no existe.", username); // Log de error si el usuario no se encuentra
                    return new UsernameNotFoundException("El usuario " + username + " no existe.");
                });

        logger.info("Usuario encontrado: {}", userEntity.getUsername()); // Log para confirmar que se encontró al usuario

        // Contraseña almacenada en la base de datos
        String storedPassword = userEntity.getPassword();
        logger.info("Contraseña almacenada: {}", storedPassword); // Log para ver la contraseña almacenada

        // Contraseña ingresada
        String enteredPassword = "123"; // Cambia esto a la contraseña ingresada por el usuario (esto es solo un ejemplo)
        logger.info("Contraseña ingresada: {}", enteredPassword); // Log para ver la contraseña ingresada

        // Verificar si la contraseña coincide
        boolean isMatch = passwordEncoder.matches(enteredPassword, storedPassword);
        logger.info("¿La contraseña coincide?: {}", isMatch); // Log del resultado de la comparación

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        // Log para verificar los roles
        logger.info("Roles asignados al usuario: {}", userEntity.getRoles());
        userEntity.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        // Log para verificar los permisos
        logger.info("Permisos asignados al usuario: {}", userEntity.getRoles());
        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        logger.info("Authorities asignados: {}", authorityList);

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authorityList);
    }


    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        AuthResponse authResponse = new AuthResponse(username, "User logged successfully", accessToken, true);
        return authResponse;
    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = this.loadUserByUsername(username);

        if (userDetails == null){
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }

        return  new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
