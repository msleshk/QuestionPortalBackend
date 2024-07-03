package com.example.QuestionPortalBackend.dto;

import com.example.QuestionPortalBackend.models.AnswerType;
//import io.swagger.annotations.ApiModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class QuestionDTO {
    private Integer id;
    @NotEmpty
    private String question;
    private String answer;
    @NotNull
    private AnswerType answerType;
    @NotEmpty
    @Email
    private String forUserEmail;
    @NotEmpty
    @Email
    private String fromUserEmail;

    private List<String> options;

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

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

    public AnswerType getAnswerType() {
        return answerType;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answerType = answerType;
    }

    public String getForUserEmail() {
        return forUserEmail;
    }

    public void setForUserEmail(String forUserEmail) {
        this.forUserEmail = forUserEmail;
    }

    public String getFromUserEmail() {
        return fromUserEmail;
    }

    public void setFromUserEmail(String fromUserEmail) {
        this.fromUserEmail = fromUserEmail;
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", answerType=" + answerType +
                ", forUserEmail='" + forUserEmail + '\'' +
                ", fromUserEmail='" + fromUserEmail + '\'' +
                ", options=" + options +
                '}';
    }
}
