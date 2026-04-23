package com.umc.librarian.service;

import com.umc.librarian.model.User;
import com.umc.librarian.model.UserRole;
import com.umc.librarian.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${default.admin.password:admin123}")
    private String defaultAdminPassword;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findById(@NonNull String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    public User register(@NonNull String name, @NonNull String email, @NonNull String password, UserRole role) {
        String normalizedEmail = email.trim().toLowerCase();
        
        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new IllegalArgumentException("Já existe um usuário com este e-mail no Librarian.");
        }

        User user = new User();
        user.setName(name.trim());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role == null ? UserRole.LEITOR : role);
        user.setActive(true);
        return userRepository.save(user);
    }

    public void createDefaultAdminIfNotExists() {
        if (!userRepository.existsByEmailIgnoreCase("admin@librarian.com")) {
            register("Administrador", "admin@librarian.com", defaultAdminPassword, UserRole.ADMIN);
        }
    }

    List<User> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}