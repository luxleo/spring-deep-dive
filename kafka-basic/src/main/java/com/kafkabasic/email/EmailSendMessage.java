package com.kafkabasic.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EmailSendMessage {
    private String from;
    private String to;
    private String subject;
    private String body;

    public static EmailSendMessage fromJson(String json) {
        var objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, EmailSendMessage.class);
    }
}
