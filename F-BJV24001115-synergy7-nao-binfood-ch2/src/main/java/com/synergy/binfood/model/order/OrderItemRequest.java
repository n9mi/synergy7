package com.synergy.binfood.model.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemRequest {
    @NotBlank(message = "order id cannot be blank")
    private String orderId;

    @NotBlank(message = "menu code cannot be blank")
    private String menuCode;

    @NotBlank(message = "variant code cannot be blank")
    private String variantCode;

    @Positive(message = "quantity must be more than 0")
    private int quantity;
}
