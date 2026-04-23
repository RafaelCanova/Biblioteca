package com.umc.librarian.repository;

import com.umc.librarian.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByPasswordResetToken(String passwordResetToken);
    boolean existsByEmailIgnoreCase(String email);
}