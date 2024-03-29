package com.synergy.binfood.model.order;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeleteOrderItemRequest {
    @NotBlank(message = "order id cannot be blank")
    private String orderId;

    @NotBlank(message = "menu code cannot be blank")
    private String menuCode;

    private String variantCode;
}
