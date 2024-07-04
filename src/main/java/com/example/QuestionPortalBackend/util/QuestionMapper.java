package com.example.QuestionPortalBackend.util;

import com.example.QuestionPortalBackend.dto.QuestionDTO;
import com.example.QuestionPortalBackend.dto.UserDTO;
import com.example.QuestionPortalBackend.models.Question;
import com.example.QuestionPortalBackend.models.QuestionOption;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionMapper {
    private final UserMapperImpl userMapper;

    public QuestionMapper(UserMapperImpl userMapper) {
        this.userMapper = userMapper;
    }

    public Question toEntity(QuestionDTO dto, UserDTO forUser, UserDTO fromUser) {
        Question question = new Question();

        question.setId(dto.getId());
        question.setQuestion(dto.getQuestion());
        question.setAnswer(dto.getAnswer());
        question.setAnswerType(dto.getAnswerType());

//        User fromUser = userMapper.toEntity(usersService
//                .getUserByEmail(dto.getFromUserEmail()));
        question.setFromUser(userMapper.toEntity(fromUser));
//        User forUser = userMapper.toEntity(usersService
//                .getUserByEmail(dto.getForUserEmail()));
        question.setForUser(userMapper.toEntity(forUser));

        List<QuestionOption> options = dto.getOptions()
                .stream()
                .map(QuestionOption::new)
                .collect(Collectors.toList());
        question.setQuestionOptions(options);

        return question;
    }

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
