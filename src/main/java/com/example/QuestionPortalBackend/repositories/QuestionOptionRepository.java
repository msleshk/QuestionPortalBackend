package com.example.QuestionPortalBackend.repositories;

import com.example.QuestionPortalBackend.models.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Integer> {
    void deleteByQuestionIsNull();
}
