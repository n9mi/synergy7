package com.synergy.binarfood.service;

import com.synergy.binarfood.model.merchant.*;
import org.springframework.data.domain.Page;

public interface MerchantService {
    public Page<MerchantResponse> findAll(MerchantGetRequest request);
    public MerchantIncomeResponse getIncomeByRange(MerchantGetIncomeRequest request);
    public MerchantResponse create(MerchantRequest request);
    public MerchantResponse update(MerchantRequest request);
    public void delete(MerchantDeleteRequest request);
}
