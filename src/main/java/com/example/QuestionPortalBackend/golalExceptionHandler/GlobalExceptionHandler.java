package com.example.QuestionPortalBackend.golalExceptionHandler;

import com.example.QuestionPortalBackend.exceptions.UserAlreadyExistsException;
import com.example.QuestionPortalBackend.exceptions.UserNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus()
    public Map<String, String> handleUserNotFoundException(UserNotFoundException exception){
        return Map.of("error", exception.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus()
    public Map<String, String> handleUserAlreadyExistsException(UserAlreadyExistsException exception){
        return Map.of("error", exception.getMessage());
    }
}
