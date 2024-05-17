package com.synergy.binarfood.model.merchant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPerMerchantResponse {
    private String productId;
    private String productName;
    private double productPrice;
    private int quantity;
    private double totalPricePerProduct;
}
