package com.techshare.Security;

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

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        logger.debug("OAuth2 Authentication success. Processing user details...");
        
        if (authentication == null) {
            logger.error("Authentication object is null");
            getRedirectStrategy().sendRedirect(request, response, "/oauth2/error?message=authentication_null");
            return;
        }

        try {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();
            
            logger.debug("OAuth2User attributes received: {}", attributes);
            
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        
            if (email == null) {
                logger.error("Email not found in OAuth2User attributes");
                getRedirectStrategy().sendRedirect(request, response, "/oauth2/error?message=email_not_found");
                return;
            }

            logger.debug("Generating JWT token for user: {}", email);
            
            // Generate JWT token
        String token = jwtUtils.generateToken(email);
        
            // Construct the success URL with the token
            String targetUrl = frontendUrl + "/oauth2/success?token=" + token;
            logger.debug("Redirecting to: {}", targetUrl);
            
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
            
        } catch (Exception e) {
            logger.error("Error during OAuth2 success handling", e);
            getRedirectStrategy().sendRedirect(request, response, 
                "/oauth2/error?message=internal_error&details=" + e.getMessage());
        }
    }
}