package com.synergy.binfood.model.order;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderRequest {
    @NotBlank(message = "order id cannot be blank")
    private String orderId;
}
