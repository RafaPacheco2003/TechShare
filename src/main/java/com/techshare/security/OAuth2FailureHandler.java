package com.techshare.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2FailureHandler.class);

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException, ServletException {
        logger.error("OAuth2 authentication failed", exception);

        String errorMessage;
        String errorDescription = "";

        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2Error oauth2Error = ((OAuth2AuthenticationException) exception).getError();
            errorMessage = oauth2Error.getErrorCode();
            errorDescription = oauth2Error.getDescription();
            logger.error("OAuth2 error code: {}, description: {}", errorMessage, errorDescription);
        } else {
            errorMessage = exception.getMessage();
            logger.error("Authentication error: {}", errorMessage);
        }

        String encodedError = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        String encodedDescription = URLEncoder.encode(errorDescription, StandardCharsets.UTF_8);

        String targetUrl = String.format("%s/oauth2/error?error=%s&error_description=%s",
                frontendUrl, encodedError, encodedDescription);

        logger.debug("Redirecting to error page: {}", targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
} 