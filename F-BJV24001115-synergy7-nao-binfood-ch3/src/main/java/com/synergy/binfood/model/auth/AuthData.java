package com.synergy.binfood.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class AuthData {
    private int userId;
    private String userName;
    private int currMerchantId;
    private int currOrderId;

    public AuthData(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public AuthData(int userId, String userName, int currMerchantId) {
        this.userId = userId;
        this.userName = userName;
        this.currMerchantId = currMerchantId;
    }
}
