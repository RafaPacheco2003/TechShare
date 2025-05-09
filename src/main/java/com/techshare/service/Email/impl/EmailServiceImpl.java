package com.techshare.service.Email.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.techshare.service.Email.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendWelcomeEmail(String email) {
        String subject = "Bienvenido a TechShare";
        String text = "Hola " + email + ",\n\n" +
                "¡Bienvenido a TechShare! Tu login ha sido exitoso.\n\n" +
                "Gracias por usar nuestra plataforma.\n\n" +
                "Saludos cordiales,\n" +
                "El equipo de TechShare";
        
        sendEmail(email, subject, text);
    }
    @Override
    public void sendVerificationRegister(String email, String token) {
        try {
            // Actualiza esta URL con tu dominio real o variable de entorno
            String verificationUrl = "http://localhost:8080/auth/verify-account?token=" + token;
            // Alternativa con variable de entorno:
            // String verificationUrl = System.getenv("APP_URL") + "/auth/verify-account?token=" + token;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Verifica tu cuenta en TechShare");
            message.setText(String.format(
                    "Hola,\n\n" +
                            "Por favor verifica tu cuenta haciendo clic en este enlace:\n" +
                            "%s\n\n" +
                            "Este enlace expirará en 5 minutos.\n\n" +
                            "Si no solicitaste este registro, ignora este mensaje.\n\n" +
                            "Equipo TechShare",
                    verificationUrl
            ));

            mailSender.send(message);
        } catch (Exception e) {

            throw new RuntimeException("Error al enviar el correo de verificación. Por favor intenta nuevamente.");
        }
    }

}
