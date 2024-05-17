package com.synergy.binarfood.service;

import com.synergy.binarfood.model.product.ProductGetByMerchantRequest;
import com.synergy.binarfood.model.product.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    public Page<ProductResponse> findAllByMerchant(ProductGetByMerchantRequest request);
}
