package com.synergy.binarfood.fcm;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {
    private final FirebaseMessaging firebaseMessaging;

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig
                .builder()
                .setAps(Aps
                        .builder()
                        .setCategory(topic)
                        .setThreadId(topic)
                        .build())
                .build();
    }

    private Message getPreconfigureMessage(NotificationContent notificationContent) {
        ApnsConfig apnsConfig = this.getApnsConfig(notificationContent.getTopic());
        Notification notification = Notification.builder()
                .setTitle(notificationContent.getTitle())
                .setBody(notificationContent.getBody())
                .build();

        return Message.builder()
                .setApnsConfig(apnsConfig)
                .setNotification(notification)
                .setToken(notificationContent.getToken())
                .build();
    }

    public void sendMessageToToken(NotificationContent notificationContent) throws InterruptedException, ExecutionException {
        Message message = this.getPreconfigureMessage(notificationContent);
        String response = this.firebaseMessaging.sendAsync(message).get();

        log.info("Message sent! Message response -> {}", response);
    }
}
