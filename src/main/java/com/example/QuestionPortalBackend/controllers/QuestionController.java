package com.example.QuestionPortalBackend.controllers;

import com.example.QuestionPortalBackend.models.Question;
import com.example.QuestionPortalBackend.services.QuestionsService;
import com.example.QuestionPortalBackend.services.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
@CrossOrigin(origins = "http://localhost:3000")
public class QuestionController {
    private final QuestionsService questionsService;

    public QuestionController(QuestionsService questionsService) {
        this.questionsService = questionsService;
    }
    @GetMapping("/user/{id}")
    public List<Question> getAllQuestionsByUser(@PathVariable("id") Integer userId){
        //TODO Map to dto and return it
        return questionsService.getQuestionsByUserId(userId);
    }
    @GetMapping("/{id}")
    public Question getQuestion(@PathVariable("id") Integer questionId){
        //TODO Map to dto and return id
        return questionsService.findOne(questionId);
    }
}
