package com.kafkabasic.email;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/emails")
@RestController
public class EmailController {
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<String> sendEmail(
            @RequestBody SendEmailRequest request
    ) {
        emailService.sendEmail(request);
        return ResponseEntity.ok("이메일 전송 완료");
    }
}
