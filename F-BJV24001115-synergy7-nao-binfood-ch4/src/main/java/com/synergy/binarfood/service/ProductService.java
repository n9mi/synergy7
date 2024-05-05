package com.synergy.binarfood.service;

import com.synergy.binarfood.model.product.ProductCreateRequest;
import com.synergy.binarfood.model.product.ProductResponse;
import com.synergy.binarfood.model.product.ProductUpdateRequest;
import org.springframework.data.domain.Page;

public interface ProductService {
    public Page<ProductResponse> findAll(int page, int pageSize, String merchantId);
    public ProductResponse create(ProductCreateRequest request);
    public ProductResponse update(ProductUpdateRequest request);
    public void delete(String id);
}
