package com.techshare.Security;

import com.techshare.Security.validation.AuthenticationValidator;
import com.techshare.entities.UserEntity;
import com.techshare.http.request.AuthLoginRequest;
import com.techshare.http.response.AuthResponse;
import com.techshare.repositories.UserRepository;
import com.techshare.service.Email.EmailService;
import com.techshare.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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

import java.util.ArrayList;
import java.util.List;
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;
    @Autowired
    private  List<AuthenticationValidator> validators;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));

        // Contraseña almacenada en la base de datos
        String storedPassword = userEntity.getPassword();

        // Contraseña ingresada
        String enteredPassword = "123"; // Cambia esto a la contraseña ingresada por el usuario (esto es solo un ejemplo)

        // Verificar si la contraseña coincide
        boolean isMatch = passwordEncoder.matches(enteredPassword, storedPassword);

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userEntity.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(
                userEntity.getUsername(),
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

        try {
            Authentication authentication = this.authenticate(username, password);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtUtils.createToken(authentication);

            emailService.sendWelcomeEmail(username);

            return new AuthResponse(username, "User logged successfully", accessToken, true);
        } catch (DisabledException e) {
            // Captura específicamente el error cuando la cuenta no está verificada
            return new AuthResponse(username, "La cuenta no está activada. Por favor verifica tu correo electrónico.", null, false);
        } catch (Exception e) {
            return new AuthResponse(username, e.getMessage(), null, false);
        }
    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = this.loadUserByUsername(username);

        // Ejecutar validaciones en orden
        validators.forEach(validator -> validator.validate(userDetails, password));
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
