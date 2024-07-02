package com.synergy.binarfood.fcm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationContent {
    private String title;
    private String body;
    private String token;
    private String topic;
}
