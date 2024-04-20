package com.synergy.binfood.model.merchant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MerchantResponse {
    private int id;
    private String merchantName;
    private String merchantLocation;
    private boolean isOpen;
}
