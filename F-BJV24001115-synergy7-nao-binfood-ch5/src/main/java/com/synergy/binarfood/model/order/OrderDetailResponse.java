package com.synergy.binarfood.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    String orderId;
    String orderDetailId;
    String merchantId;
    String merchantName;
    String productId;
    String productName;
    int quantity;
    double totalPrice;
}
