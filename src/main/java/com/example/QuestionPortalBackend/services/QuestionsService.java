package com.example.QuestionPortalBackend.services;

import com.example.QuestionPortalBackend.dto.QuestionDTO;

import java.util.List;

public interface QuestionsService {

    List<QuestionDTO> getQuestionsByUserId(int userId);

    List<QuestionDTO> getQuestionsByForUserId(int userId);

    void setAnswer(int id, QuestionDTO questionDTO);

    QuestionDTO findOne(int questionId);

    QuestionDTO addQuestion(QuestionDTO questionDTO);

    QuestionDTO updateQuestion(int id, QuestionDTO questionDTO);

    void deleteQuestion(int id);
}
