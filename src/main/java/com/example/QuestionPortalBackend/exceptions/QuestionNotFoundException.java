package com.example.QuestionPortalBackend.exceptions;

public class QuestionNotFoundException extends RuntimeException{
    public QuestionNotFoundException(String message){
        super(message);
    }
}
