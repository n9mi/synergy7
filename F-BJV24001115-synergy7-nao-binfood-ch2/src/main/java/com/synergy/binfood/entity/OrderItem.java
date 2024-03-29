package com.synergy.binfood.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class OrderItem {
    private String orderId;
    private String menuCode;
    private String variantCode;
    private int quantity;
}
