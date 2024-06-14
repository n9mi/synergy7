package com.synergy.binarfood.mail.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailMessage <T> {
    private String recipient;

    private MailType type;

    private T data;
}
