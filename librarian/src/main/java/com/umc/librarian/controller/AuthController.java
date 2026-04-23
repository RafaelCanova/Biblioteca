package com.umc.librarian.controller;

import com.umc.librarian.service.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class AuthController{

    private final PasswordResetService passwordResetService;

    public AuthController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String requestPasswordReset(@RequestParam String email,
                                       HttpServletRequest request,
                                       RedirectAttributes redirectAttributes) {
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .replaceQuery(null)
                .build()
                .toUriString();

        passwordResetService.requestPasswordReset(email, baseUrl);
        redirectAttributes.addFlashAttribute("success", "Se o e-mail existir, enviaremos o link.");
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String updatePassword(@RequestParam String token,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "As senhas não conferem.");
            return "redirect:/reset-password?token=" + token;
        }

        try {
            passwordResetService.resetPassword(token, password);
            redirectAttributes.addFlashAttribute("success", "Senha alterada. Faça login.");
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/reset-password?token=" + token;
        }
    }
}