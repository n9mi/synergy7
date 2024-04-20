package com.synergy.binfood.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Product {
    private int id;
    private String productName;
    private int price;
    private int merchantId;
}
