package com.example.QuestionPortalBackend.repositories;

import com.example.QuestionPortalBackend.models.Question;
import com.example.QuestionPortalBackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionsRepository extends JpaRepository<Question, Integer> {
    List<Question> getQuestionsByForUser(User user);
}
