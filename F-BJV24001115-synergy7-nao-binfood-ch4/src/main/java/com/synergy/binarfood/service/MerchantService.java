package com.synergy.binarfood.service;

import com.synergy.binarfood.model.merchant.MerchantCreateRequest;
import com.synergy.binarfood.model.merchant.MerchantResponse;
import com.synergy.binarfood.model.merchant.MerchantUpdateRequest;
import org.springframework.data.domain.Page;

public interface MerchantService {
    public Page<MerchantResponse> findAll(int page, int pageSize);
    public MerchantResponse findById(String id);
    public MerchantResponse create(MerchantCreateRequest request);
    public MerchantResponse update(MerchantUpdateRequest request);
    public void delete(String id);
}
