package com.example.QuestionPortalBackend.repositories;

import com.example.QuestionPortalBackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsUserByEmail(String email);
}
