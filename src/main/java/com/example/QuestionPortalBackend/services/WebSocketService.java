package com.example.QuestionPortalBackend.services;

import com.example.QuestionPortalBackend.dto.QuestionDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendQuestionUpdate(QuestionDTO questionToSend) {
        messagingTemplate.convertAndSend("/topic/questions", questionToSend);
    }

    public void sendQuestionDelete(int id){
        messagingTemplate.convertAndSend("/topic/questions/delete", id);
    }
}
