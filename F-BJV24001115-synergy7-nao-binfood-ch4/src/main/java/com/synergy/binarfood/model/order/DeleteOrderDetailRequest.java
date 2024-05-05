package com.synergy.binarfood.model.order;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteOrderDetailRequest {
    @NotBlank
    private String orderId;

    @NotBlank
    private String orderDetailId;
}
