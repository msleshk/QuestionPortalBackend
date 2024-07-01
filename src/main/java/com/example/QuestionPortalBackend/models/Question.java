package com.example.QuestionPortalBackend.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "question")
    private String question;
    @Column(name = "answer")
    private String answer;
    @Enumerated(EnumType.STRING)
    @Column(name = "answer_type")
    private AnswerType answerType;
    @OneToMany(mappedBy = "question", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<QuestionOption> questionOptions;
    @ManyToOne
    @JoinColumn(name = "for_user_id", referencedColumnName = "id")
    private User forUser;
    @ManyToOne
    @JoinColumn(name = "from_user_id", referencedColumnName = "id")
    private User fromUser;

    public List<QuestionOption> getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(List<QuestionOption> questionOptions) {
        this.questionOptions = questionOptions;
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

    public User getForUser() {
        return forUser;
    }

    public void setForUser(User forUser) {
        this.forUser = forUser;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }
}
