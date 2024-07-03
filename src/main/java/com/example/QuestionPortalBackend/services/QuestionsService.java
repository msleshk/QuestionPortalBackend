package com.example.QuestionPortalBackend.services;

import com.example.QuestionPortalBackend.dto.QuestionDTO;

import java.util.List;

public interface QuestionsService {

    public List<QuestionDTO> getQuestionsByUserId(int userId);

    public List<QuestionDTO> getQuestionsByForUserId(int userId);

    public void setAnswer(int id, QuestionDTO questionDTO);

    public QuestionDTO findOne(int questionId);

    public QuestionDTO addQuestion(QuestionDTO questionDTO);

    public QuestionDTO updateQuestion(int id, QuestionDTO questionDTO);

    public void deleteQuestion(int id);
}
