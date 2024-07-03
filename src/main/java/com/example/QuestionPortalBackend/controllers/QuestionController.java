package com.example.QuestionPortalBackend.controllers;

import com.example.QuestionPortalBackend.dto.QuestionDTO;
import com.example.QuestionPortalBackend.services.QuestionsService;
import com.example.QuestionPortalBackend.services.QuestionsServiceImpl;
import com.example.QuestionPortalBackend.services.WebSocketService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/questions")
@CrossOrigin(origins = "http://localhost:3000")
public class QuestionController {
    private final QuestionsService questionsService;
    private final WebSocketService webSocketService;

    public QuestionController(QuestionsService questionsService, WebSocketService webSocketService) {
        this.questionsService = questionsService;
        this.webSocketService = webSocketService;
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Get all questions by user ID", description = "Fetches all questions associated with a specific user ID")
    public List<QuestionDTO> getAllQuestionsByFromUser(@PathVariable("id") Integer userId) {
        return questionsService.getQuestionsByUserId(userId);
    }

    @GetMapping("/forUser/{id}")
    public List<QuestionDTO> getAllQuestionsByForUser(@PathVariable("id") Integer userId) {
        return questionsService.getQuestionsByForUserId(userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a question by ID", description = "Fetches a specific question by its ID")
    public QuestionDTO getQuestion(@PathVariable("id") Integer questionId) {
        return questionsService.findOne(questionId);
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new question", description = "Adds a new question and returns the saved question")
    public QuestionDTO addQuestion(@RequestBody @Valid QuestionDTO questionDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        QuestionDTO savedQuestion = questionsService.addQuestion(questionDTO);
        webSocketService.sendQuestionUpdate(savedQuestion);
        return savedQuestion;
    }

    @PatchMapping("/answer/{id}")
    @Operation(summary = "Set answer for a question", description = "Sets answer for an existing question by its ID")
    public void addAnswerForQuestion(@PathVariable("id") Integer questionId, @RequestBody @Valid QuestionDTO questionDTO) {
        questionsService.setAnswer(questionId, questionDTO);
        webSocketService.sendQuestionUpdate(questionDTO);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a question", description = "Updates an existing question by its ID")
    public QuestionDTO updateQuestion(@PathVariable("id") Integer questionId, @RequestBody @Valid QuestionDTO questionDTO) {
        QuestionDTO updatedQuestion = questionsService.updateQuestion(questionId, questionDTO);
        System.out.println("Sending updated question to user: " + questionDTO.getFromUserEmail());
        System.out.println(updatedQuestion.toString());
        webSocketService.sendQuestionUpdate(updatedQuestion);
        System.out.println(questionDTO.getFromUserEmail());
        return updatedQuestion;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a question", description = "Deletes an existing question by its ID")
    public void deleteQuestion(@PathVariable Integer id) {
        questionsService.deleteQuestion(id);
    }
}
