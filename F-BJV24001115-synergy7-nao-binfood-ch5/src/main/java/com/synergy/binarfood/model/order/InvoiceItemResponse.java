package com.synergy.binarfood.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceItemResponse {
    String merchantName;
    String productName;
    String productQuantity;
    String productPrice;
    String productTotalPrice;
}
