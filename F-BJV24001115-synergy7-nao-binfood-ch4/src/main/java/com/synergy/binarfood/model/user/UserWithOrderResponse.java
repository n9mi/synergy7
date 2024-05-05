package com.synergy.binarfood.model.user;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWithOrderResponse {
    String id;
    String username;
    String email;
    String createdDate;
    int totalOrder;
}
