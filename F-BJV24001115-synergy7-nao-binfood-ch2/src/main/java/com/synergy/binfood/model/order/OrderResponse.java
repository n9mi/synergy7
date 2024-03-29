package com.synergy.binfood.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class OrderResponse {
    private String orderId;
    private Date payAt;
    private List<OrderItemResponse> orderItems;
    private int totalPrice;

    public OrderResponse(String orderId) {
        this.orderId = orderId;
    }
}
