package com.example.QuestionPortalBackend.controllers;

import com.example.QuestionPortalBackend.dto.QuestionDTO;
import com.example.QuestionPortalBackend.services.QuestionsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/questions")
@CrossOrigin(origins = "http://localhost:3000")
public class QuestionController {
    private final QuestionsService questionsService;
    private final SimpMessagingTemplate messagingTemplate;

    public QuestionController(QuestionsService questionsService, SimpMessagingTemplate messagingTemplate) {
        this.questionsService = questionsService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Get all questions by user ID", description = "Fetches all questions associated with a specific user ID")
    public List<QuestionDTO> getAllQuestionsByUser(@PathVariable("id") Integer userId) {
        System.out.println("all users");
        return questionsService.getQuestionsByUserId(userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a question by ID", description = "Fetches a specific question by its ID")
    public QuestionDTO getQuestion(@PathVariable("id") Integer questionId) {
        return questionsService.findOne(questionId);
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new question", description = "Adds a new question and returns the saved question")
    public QuestionDTO addQuestion(@RequestBody @Valid QuestionDTO questionDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        QuestionDTO savedQuestion = questionsService.addQuestion(questionDTO);
        messagingTemplate.convertAndSend("/topic/questions", savedQuestion);
        return savedQuestion;
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a question", description = "Updates an existing question by its ID")
    public void updateQuestion(@PathVariable("id") Integer questionId, @RequestBody @Valid QuestionDTO questionDTO) {
        QuestionDTO updatedQuestion = questionsService.updateQuestion(questionId, questionDTO);
        messagingTemplate.convertAndSendToUser(questionDTO.getForUserEmail(), "/queue/questions", questionDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a question", description = "Deletes an existing question by its ID")
    public void deleteQuestion(@PathVariable Integer id) {
        questionsService.deleteQuestion(id);
    }
}
