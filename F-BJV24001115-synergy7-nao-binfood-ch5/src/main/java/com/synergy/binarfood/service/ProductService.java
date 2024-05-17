package com.synergy.binarfood.service;

import com.synergy.binarfood.model.product.ProductDeleteRequest;
import com.synergy.binarfood.model.product.ProductGetByMerchantRequest;
import com.synergy.binarfood.model.product.ProductRequest;
import com.synergy.binarfood.model.product.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    public Page<ProductResponse> findAllByMerchant(ProductGetByMerchantRequest request);
    public ProductResponse create(ProductRequest request);
    public ProductResponse update(ProductRequest request);
    public void delete(ProductDeleteRequest request);
}
