package com.example.QuestionPortalBackend.services;

import com.example.QuestionPortalBackend.dto.QuestionDTO;
import com.example.QuestionPortalBackend.exceptions.QuestionNotFoundException;
import com.example.QuestionPortalBackend.exceptions.UserNotFoundException;
import com.example.QuestionPortalBackend.models.Question;
import com.example.QuestionPortalBackend.models.QuestionOption;
import com.example.QuestionPortalBackend.models.User;
import com.example.QuestionPortalBackend.repositories.QuestionsRepository;
import com.example.QuestionPortalBackend.util.QuestionMapper;
import com.example.QuestionPortalBackend.util.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class QuestionsService {
    private final QuestionsRepository questionsRepository;
    private final UsersService usersService;
    private final UserMapper userMapper;
    private final QuestionMapper questionMapper;

    public QuestionsService(QuestionsRepository questionsRepository, UsersService usersService, UserMapper userMapper, QuestionMapper questionMapper) {
        this.questionsRepository = questionsRepository;
        this.usersService = usersService;
        this.userMapper = userMapper;
        this.questionMapper = questionMapper;
    }

    public List<QuestionDTO> getQuestionsByUserId(int userId) {
        User user = userMapper.toEntity(usersService.findOne(userId));
        List<Question> questions = questionsRepository.getQuestionsByFromUser(user);
        return questions.stream()
                .map(questionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public QuestionDTO findOne(int questionId) {
        Optional<Question> questionOptional = questionsRepository.findById(questionId);
        return questionMapper.toDTO(questionOptional
                .orElseThrow(() -> new QuestionNotFoundException("Question not found")));
    }

    @Transactional
    public QuestionDTO addQuestion(QuestionDTO questionDTO) {
        Question question = questionMapper.toEntity(questionDTO);
        if (question.getQuestionOptions() != null) {
            for (QuestionOption option : question.getQuestionOptions()) {
                option.setQuestion(question);
            }
        }
        questionsRepository.save(question);
        return questionMapper.toDTO(question);
    }

    @Transactional
    public QuestionDTO updateQuestion(int id, QuestionDTO questionDTO) {
        Question existingQuestion = questionsRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("Question with id " + id + " not found"));

        Question questionToUpdate = questionMapper.toEntity(questionDTO);
        questionToUpdate.setId(id);

        questionsRepository.save(questionToUpdate);
        return questionMapper.toDTO(questionToUpdate);
    }

    @Transactional
    public void deleteQuestion(int id){
        questionsRepository.deleteById(id);
    }
}
