package com.synergy.binarfood.model.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderDetailRequest {
    @NotBlank
    private String orderId;

    @NotBlank
    private String productId;

    @Positive
    private int quantity;
}
