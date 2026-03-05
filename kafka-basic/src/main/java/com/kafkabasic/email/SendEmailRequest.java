package com.kafkabasic.email;

import lombok.Getter;

@Getter
public class SendEmailRequest {
    private String from;
    private String to;
    private String subject;
    private String body;
}
