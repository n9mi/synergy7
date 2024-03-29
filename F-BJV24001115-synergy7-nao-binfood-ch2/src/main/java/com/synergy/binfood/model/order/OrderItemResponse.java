package com.synergy.binfood.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class OrderItemResponse {
    private String menuCode;
    private String menuName;
    private int menuPrice;
    private String variantCode;
    private String variantName;
    private int quantity;
    private int totalPerItem;
}
