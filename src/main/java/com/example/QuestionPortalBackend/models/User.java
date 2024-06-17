package com.example.QuestionPortalBackend.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Users")
public class User {
    //TODO Сделать валидацию
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "forUser", orphanRemoval = true)
    private List<Question> questionsForUser;
    @OneToMany(mappedBy = "fromUser", orphanRemoval = true)
    private List<Question> questionsFromUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Question> getQuestionsForUser() {
        return questionsForUser;
    }

    public void setQuestionsForUser(List<Question> questionsForUser) {
        this.questionsForUser = questionsForUser;
    }

    public List<Question> getQuestionsFromUser() {
        return questionsFromUser;
    }

    public void setQuestionsFromUser(List<Question> questionsFromUser) {
        this.questionsFromUser = questionsFromUser;
    }
}
