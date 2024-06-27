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
    public ResponseEntity<?> getAllQuestionsByUser(@PathVariable("id") Integer userId){
        //TODO Map to dto and return it
        List<Question> questionsForUser=questionsService.getQuestionsByUserId(userId);
        return ResponseEntity.ok(questionsForUser);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestion(@PathVariable("id") Integer questionId){
        //TODO Map to dto and return id
        Question question= questionsService.findOne(questionId);
        return ResponseEntity.ok(question);
    }
}
