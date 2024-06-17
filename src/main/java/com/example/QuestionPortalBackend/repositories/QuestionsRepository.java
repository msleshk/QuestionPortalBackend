package com.example.QuestionPortalBackend.repositories;

import com.example.QuestionPortalBackend.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionsRepository extends JpaRepository<Question, Integer> {
}
