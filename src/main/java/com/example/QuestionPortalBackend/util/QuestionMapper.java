package com.example.QuestionPortalBackend.util;

import com.example.QuestionPortalBackend.dto.QuestionDTO;
import com.example.QuestionPortalBackend.models.Question;
import com.example.QuestionPortalBackend.models.QuestionOption;
import com.example.QuestionPortalBackend.models.User;
import com.example.QuestionPortalBackend.services.UsersService;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionMapper implements MapperInterface<Question, QuestionDTO>{
    private final UsersService usersService;
    private final UserMapper userMapper;

    public QuestionMapper(UsersService usersService, UserMapper userMapper) {
        this.usersService = usersService;
        this.userMapper = userMapper;
    }

    @Override
    public Question toEntity(QuestionDTO dto) {
        Question question = new Question();

        question.setId(dto.getId());
        question.setQuestion(dto.getQuestion());
        question.setAnswer(dto.getAnswer());
        question.setAnswerType(dto.getAnswerType());

        User fromUser = userMapper.toEntity(usersService
                .getUserByEmail(dto.getFromUserEmail()));
        question.setFromUser(fromUser);
        User forUser = userMapper.toEntity(usersService
                .getUserByEmail(dto.getForUserEmail()));
        question.setForUser(forUser);

        List<QuestionOption> options = dto.getOptions()
                        .stream()
                        .map(QuestionOption::new)
                        .collect(Collectors.toList());
        question.setQuestionOptions(options);

        return question;
    }

    @Override
    public QuestionDTO toDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();

        questionDTO.setId(question.getId());
        questionDTO.setQuestion(question.getQuestion());
        questionDTO.setAnswer(question.getAnswer());
        questionDTO.setAnswerType(question.getAnswerType());
        questionDTO.setFromUserEmail(question.getFromUser().getEmail());
        questionDTO.setForUserEmail(question.getForUser().getEmail());

        questionDTO.setOptions(question.getQuestionOptions()
                .stream()
                .map(QuestionOption::getOptionText)
                .collect(Collectors.toList()));

        return questionDTO;
    }
}
