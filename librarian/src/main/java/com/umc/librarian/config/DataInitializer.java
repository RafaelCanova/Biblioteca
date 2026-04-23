package com.umc.librarian.config;

import com.umc.librarian.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initDefaultAdmin(UserService userService) {
        return args -> {
            try {
                userService.createDefaultAdminIfNotExists();
                logger.info("Verificação de usuário administrador concluída.");
            } catch (DataAccessException ex) {
                logger.warn(
                        "O MongoDB está indisponível. O administrador padrão não foi inicializado. " +
                        "Certifique-se de que o banco de dados está rodando para o funcionamento da EAP 3.1.",
                        ex
                );
            }
        };
    }
}