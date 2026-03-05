package com.kafkabasic.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailSendConsumer {

    @KafkaListener(
            topics = "email.send",
            groupId = "email-send-group"
    )
    public void consume(String message) {
        log.info("message from kafka: {}", message);
        var sendMessage = EmailSendMessage.fromJson(message);
        // 이메일 전송로직 생략
        sleep(4000);
        log.info("이메일 발송 완료");
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException("sleep error");
        }
    }
}
