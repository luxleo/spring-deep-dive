package com.kafkabasic.email;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendEmail(SendEmailRequest request) {
        var emailSendMessage = new EmailSendMessage(
                request.getFrom(),
                request.getTo(),
                request.getSubject(),
                request.getBody()
        );
        String emailJson = objectMapper.writeValueAsString(emailSendMessage);
        kafkaTemplate.send("email.send", emailJson);
    }

}
