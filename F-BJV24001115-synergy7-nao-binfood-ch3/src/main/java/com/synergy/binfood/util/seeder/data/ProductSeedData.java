package com.synergy.binfood.util.seeder.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ProductSeedData {
    private String productName;
    private int priceLowerBound;
    private int priceHigherBound;
}
