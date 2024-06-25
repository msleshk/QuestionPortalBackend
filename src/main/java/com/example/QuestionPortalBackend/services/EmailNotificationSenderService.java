package com.example.QuestionPortalBackend.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationSenderService {

    private final JavaMailSender javaMailSender;

    public EmailNotificationSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendNotification(String email, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("maximo4kahd@mail.ru");

        javaMailSender.send(message);
    }
}
