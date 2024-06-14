package com.synergy.binarfood.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synergy.binarfood.core.message.MailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailPublisherService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String MAIL_TOPIC = "mail-registration-otp";

    public <T> void publish(MailMessage<T> mailMessage) throws JsonProcessingException {
        String mailMessageStr = this.objectMapper.writeValueAsString(mailMessage);
        kafkaTemplate.send(MAIL_TOPIC, mailMessageStr);
    }
}
