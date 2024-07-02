package com.synergy.binarfood.scheduler;

import com.synergy.binarfood.fcm.FCMService;
import com.synergy.binarfood.fcm.NotificationContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class PromotionScheduler {
    @Value("${app.firebase.messaging.client.token}")
    private String firebaseMessagingClientToken;

    private final FCMService fcmService;

    @Scheduled(cron = "0 0 11,12 * * *", zone="Asia/Jakarta")
    public void broadcastLunchPromotion() throws ExecutionException, InterruptedException {
        log.info("Message scheduler called");
        NotificationContent notificationContent = NotificationContent.builder()
                .title("Lunch promo Binarfood")
                .body("Silahkan pesan pukul 12.00 - 12.59 untuk mendapatkan promo 20%")
                .token(this.firebaseMessagingClientToken)
                .build();
        this.fcmService.sendMessageToToken(notificationContent);
    }
}
