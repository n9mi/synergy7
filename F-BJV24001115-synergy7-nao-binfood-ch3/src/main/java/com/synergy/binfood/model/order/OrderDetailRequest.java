package com.synergy.binfood.model.order;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class OrderDetailRequest {
    @Positive(message =  "invalid product id")
    private int productId;

    @Positive(message = "quantity should be more than 0")
    private int quantity;
}
