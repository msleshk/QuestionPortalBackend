package com.example.QuestionPortalBackend.services;

import com.example.QuestionPortalBackend.dto.QuestionDTO;
import com.example.QuestionPortalBackend.models.Question;
import com.example.QuestionPortalBackend.models.User;
import com.example.QuestionPortalBackend.repositories.QuestionsRepository;
import com.example.QuestionPortalBackend.util.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class QuestionsService {
    private final QuestionsRepository questionsRepository;
    private final UsersService usersService;
    private final UserMapper userMapper;

    public QuestionsService(QuestionsRepository questionsRepository, UsersService usersService, UserMapper userMapper) {
        this.questionsRepository = questionsRepository;
        this.usersService = usersService;
        this.userMapper = userMapper;
    }

    public List<Question> getQuestionsByUserId(int userId) {
        //TODO change return to questionDTO
        User user=userMapper.toEntity(usersService.findOne(userId));
        return questionsRepository.getQuestionsByForUser(user);
    }

    public Question findOne(int questionId) {
        Optional<Question> questionOptional = questionsRepository.findById(questionId);
        return questionOptional.orElse(null);
    }
}
