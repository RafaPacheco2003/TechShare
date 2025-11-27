package com.techshare.security;

import com.techshare.services.user.UserProvisioningService;
import com.techshare.util.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Manejador de autenticación exitosa para OAuth2 (Google).
 * Se encarga de crear usuarios automáticamente si no existen y generar el token JWT.
 */
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    private final JwtUtils jwtUtils;
    private final UserProvisioningService userProvisioningService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    public OAuth2SuccessHandler(JwtUtils jwtUtils, UserProvisioningService userProvisioningService) {
        this.jwtUtils = jwtUtils;
        this.userProvisioningService = userProvisioningService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        logger.debug("OAuth2 authentication successful. Processing user details...");
        
        if (authentication == null) {
            logger.error("Authentication object is null");
            redirectToError(request, response, "authentication_null");
            return;
        }

        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();
            
            logger.debug("OAuth2User attributes: {}", attributes);
            
            // Extraer información del usuario de Google
            String email = oAuth2User.getAttribute("email");
            String givenName = oAuth2User.getAttribute("given_name");
            String familyName = oAuth2User.getAttribute("family_name");
            
            if (email == null || email.isEmpty()) {
                logger.error("Email not found in OAuth2User attributes");
                redirectToError(request, response, "email_not_found");
                return;
            }

            // Buscar o crear usuario en la base de datos
            userProvisioningService.findOrCreateUser(email, givenName, familyName);
            
            logger.debug("Generating JWT token for user: {}", email);
            
            // Generar token JWT con la autenticación completa
            String token = jwtUtils.createToken(authentication);
            
            // Redirigir al frontend con el token
            String targetUrl = frontendUrl + "/oauth2/success?token=" + token;
            logger.debug("Redirecting to: {}", targetUrl);
            
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
            
        } catch (Exception e) {
            logger.error("Error during OAuth2 success handling", e);
            redirectToError(request, response, "internal_error");
        }
    }

    /**
     * Redirige al usuario a la página de error del frontend.
     */
    private void redirectToError(HttpServletRequest request, HttpServletResponse response, String errorCode) 
            throws IOException {
        String errorUrl = frontendUrl + "/oauth2/error?message=" + errorCode;
        getRedirectStrategy().sendRedirect(request, response, errorUrl);
    }
}