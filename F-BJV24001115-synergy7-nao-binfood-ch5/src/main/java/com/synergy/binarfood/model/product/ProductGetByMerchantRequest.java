package com.synergy.binarfood.model.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductGetByMerchantRequest {
    private int page;
    private int pageSize;
    private String merchantId;
}
