package com.synergy.binfood.model.merchant;

import com.synergy.binfood.model.product.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class MerchantWithProductsResponse {
    private int merchantId;
    private String merchantName;
    private String merchantLocation;
    private boolean merchantOpen;
    private List<ProductResponse> merchantProducts;
}
