package com.example.QuestionPortalBackend.dto;

public class QuestionDTO {
    private Integer id;
    private String question;
    private String answer;
    private String answerType;
    private Integer forUserId;
    private Integer fromUserId;

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

    public Integer getForUserId() {
        return forUserId;
    }

    public void setForUserId(Integer forUserId) {
        this.forUserId = forUserId;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }
}
