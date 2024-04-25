package com.synergy.binfood.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class OrderDetailResponse {
    private int id;
    private String productName;
    private int productQuantity;
    private int productPrice;
    private int totalPrice;
}
