package com.example.QuestionPortalBackend.DTO;

import com.example.QuestionPortalBackend.models.AnswerType;
import com.example.QuestionPortalBackend.models.User;
import jakarta.persistence.*;

public class QuestionDTO {
    private Integer id;
    private String question;
    private String answer;
    private String answerType;
    private Integer forUser_id;
    private Integer fromUser_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public Integer getForUser_id() {
        return forUser_id;
    }

    public void setForUser_id(Integer forUser_id) {
        this.forUser_id = forUser_id;
    }

    public Integer getFromUser_id() {
        return fromUser_id;
    }

    public void setFromUser_id(Integer fromUser_id) {
        this.fromUser_id = fromUser_id;
    }
}
