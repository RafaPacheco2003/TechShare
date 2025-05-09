package com.techshare.service.Email;

/**
 * Interfaz para el servicio de envío de correos electrónicos
 */
public interface EmailService {

    /**
     * Envía un correo electrónico
     * @param to Destinatario
     * @param subject Asunto
     * @param text Contenido del correo
     */
    void sendEmail(String to, String subject, String text);

    /**
     * Envía un correo de bienvenida al usuario
     * @param email Email del usuario
     */
    void sendWelcomeEmail(String email);


    void sendVerificationRegister(String email, String token);
}
