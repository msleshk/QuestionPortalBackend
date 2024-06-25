package com.example.QuestionPortalBackend.services;

import com.example.QuestionPortalBackend.models.Question;
import com.example.QuestionPortalBackend.repositories.QuestionsRepository;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class QuestionsService {
    private final QuestionsRepository questionsRepository;
    private final UsersService usersService;

    public QuestionsService(QuestionsRepository questionsRepository, UsersService usersService) {
        this.questionsRepository = questionsRepository;
        this.usersService = usersService;
    }

    public List<Question> getQuestionsByUserId(int userId) {
        return questionsRepository.getQuestionsByForUser(usersService.findOne(userId));
    }

    public Question findOne(int questionId) {
        Optional<Question> questionOptional = questionsRepository.findById(questionId);
        return questionOptional.orElse(null);
    }
}
