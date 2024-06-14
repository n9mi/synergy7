package com.synergy.binarfood.mail.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synergy.binarfood.mail.message.MailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailConsumerService {
    private final JavaMailSender javaMailSender;
    private final ObjectMapper objectMapper;

    @Value("${spring.mail.username}")
    private String mailSender;

    public void sendMail(String mailRecipient, String mailSubject, String mailMessage) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(this.mailSender);
        mail.setTo(mailRecipient);
        mail.setSubject(mailSubject);
        mail.setText(mailMessage);

        this.javaMailSender.send(mail);
    }

    @KafkaListener(
            topics = "mail-registration-otp",
            groupId = "group_id_config",
            containerFactory = "mailMessageListener")
    public <T> void consumeRegistrationOtp(String mailMessageStr) throws JsonMappingException, JsonProcessingException {
        MailMessage<String> mailMessage = this.objectMapper.readValue(mailMessageStr,
                new TypeReference<MailMessage<String>>() {});
        this.sendMail(mailMessage.getRecipient(), "BINARFOOD REGISTRATION OTP", mailMessage.getData());

        log.info("[ REGISTRATION OTP SENT ] : " + mailMessageStr);
    }
}
