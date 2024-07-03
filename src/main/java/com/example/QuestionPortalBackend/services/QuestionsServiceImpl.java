package com.example.QuestionPortalBackend.services;

import com.example.QuestionPortalBackend.dto.QuestionDTO;
import com.example.QuestionPortalBackend.dto.UserDTO;
import com.example.QuestionPortalBackend.exceptions.QuestionNotFoundException;
import com.example.QuestionPortalBackend.models.Question;
import com.example.QuestionPortalBackend.models.QuestionOption;
import com.example.QuestionPortalBackend.models.User;
import com.example.QuestionPortalBackend.repositories.QuestionOptionRepository;
import com.example.QuestionPortalBackend.repositories.QuestionsRepository;
import com.example.QuestionPortalBackend.util.QuestionMapper;
import com.example.QuestionPortalBackend.util.UserMapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class QuestionsServiceImpl implements QuestionsService {
    private final QuestionsRepository questionsRepository;
    private final UsersService usersService;
    private final UserMapperImpl userMapper;
    private final QuestionMapper questionMapper;
    private final QuestionOptionRepository questionOptionRepository;

    public QuestionsServiceImpl(QuestionsRepository questionsRepository, UsersService usersService, UserMapperImpl userMapper, QuestionMapper questionMapper, QuestionOptionRepository questionOptionRepository) {
        this.questionsRepository = questionsRepository;
        this.usersService = usersService;
        this.userMapper = userMapper;
        this.questionMapper = questionMapper;
        this.questionOptionRepository = questionOptionRepository;
    }

    public List<QuestionDTO> getQuestionsByUserId(int userId) {
        User user = userMapper.toEntity(usersService.findOne(userId));
        List<Question> questions = questionsRepository.getQuestionsByFromUser(user);
        return questions.stream()
                .map(questionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<QuestionDTO> getQuestionsByForUserId(int userId) {
        User user = userMapper.toEntity(usersService.findOne(userId));
        List<Question> questions = questionsRepository.getQuestionsByForUser(user);
        return questions.stream()
                .map(questionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void setAnswer(int id, QuestionDTO questionDTO) {
        Question existingQuestion = questionsRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("Question with id " + id + " not found"));
        existingQuestion.setAnswer(questionDTO.getAnswer());
    }

    public QuestionDTO findOne(int questionId) {
        Optional<Question> questionOptional = questionsRepository.findById(questionId);
        return questionMapper.toDTO(questionOptional
                .orElseThrow(() -> new QuestionNotFoundException("Question not found")));
    }

    @Transactional
    public QuestionDTO addQuestion(QuestionDTO questionDTO) {
        UserDTO forUser = usersService.getUserByEmail(questionDTO.getForUserEmail());
        UserDTO fromUser = usersService.getUserByEmail(questionDTO.getFromUserEmail());
        Question question = questionMapper.toEntity(questionDTO, forUser, fromUser);
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

        existingQuestion.setQuestion(questionDTO.getQuestion());
        existingQuestion.setAnswer(questionDTO.getAnswer());
        existingQuestion.setAnswerType(questionDTO.getAnswerType());
        existingQuestion.getQuestionOptions().clear();
        existingQuestion.setAnswer(null);

        List<QuestionOption> optionList = questionDTO.getOptions()
                .stream()
                .map(optionText -> {
                    QuestionOption option = new QuestionOption(optionText);
                    option.setQuestion(existingQuestion);
                    return option;
                })
                .toList();

        existingQuestion.getQuestionOptions().addAll(optionList);
        questionsRepository.save(existingQuestion);
        questionOptionRepository.deleteByQuestionIsNull();
        return questionMapper.toDTO(existingQuestion);
    }

    @Transactional
    public void deleteQuestion(int id) {
        questionsRepository.deleteById(id);
    }
}
