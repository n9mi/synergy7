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
public class OrderDetailGetRequest {
    int page;
    int pageSize;

    @NotBlank
    String username;

    @NotBlank
    String orderId;
}
