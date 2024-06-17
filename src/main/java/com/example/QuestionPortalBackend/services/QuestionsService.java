package com.example.QuestionPortalBackend.services;

import com.example.QuestionPortalBackend.repositories.QuestionsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class QuestionsService {
    private final QuestionsRepository questionsRepository;

    public QuestionsService(QuestionsRepository questionsRepository) {
        this.questionsRepository = questionsRepository;
    }
}
