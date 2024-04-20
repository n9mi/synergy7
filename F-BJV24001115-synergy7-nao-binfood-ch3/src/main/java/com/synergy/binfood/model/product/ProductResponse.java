package com.synergy.binfood.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ProductResponse {
    private int id;
    private String productName;
    private int price;
}
