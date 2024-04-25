package com.synergy.binfood.model.merchant;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class GetMerchantProductRequest {
    @Positive(message =  "invalid merchant id")
    private int merchantId;

    @Positive(message =  "invalid product id")
    private int productId;
}
