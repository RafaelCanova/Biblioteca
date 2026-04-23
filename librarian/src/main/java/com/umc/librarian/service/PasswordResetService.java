package com.umc.librarian.service;

import com.umc.librarian.model.User;
import com.umc.librarian.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);
    private static final int TOKEN_BYTES = 32;
    private static final int TOKEN_EXPIRATION_MINUTES = 30;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.mail.from:no-reply@librarian.com}")
    private String fromEmail;

    public PasswordResetService(UserRepository userRepository,
                                PasswordEncoder passwordEncoder,
                                JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public void createAndSendToken(String email, String baseUrl) {
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);
        if (userOpt.isEmpty()) {
            return;
        }

        User user = userOpt.get();
        String token = generateToken();
        user.setPasswordResetToken(token);
        user.setPasswordResetTokenExpiresAt(LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_MINUTES));
        userRepository.save(user);

        sendResetEmail(user, buildResetLink(baseUrl, token));
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token de recuperação inválido."));

        if (user.getPasswordResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("O token de recuperação expirou.");
        }

        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("A nova senha deve ter pelo menos 6 caracteres.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiresAt(null);
        userRepository.save(user);
    }

    private String generateToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String buildResetLink(String baseUrl, String token) {
        return baseUrl + "/reset-password?token=" + token;
    }

    private void sendResetEmail(User user, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Recuperação de Senha - Librarian");
        message.setText("""
                Olá, %s.

                Recebemos uma solicitação para redefinir sua senha no sistema Librarian.

                Acesse o link abaixo para criar uma nova senha:
                %s

                Este link expira em %d minutos.

                Se você não solicitou a recuperação, ignore este e-mail.
                """.formatted(user.getName(), resetLink, TOKEN_EXPIRATION_MINUTES));

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            logger.warn("Não foi possível enviar o e-mail de recuperação. Link gerado: {}", resetLink, ex);
        }
    }
}