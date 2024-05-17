package com.synergy.binarfood.service;

import com.synergy.binarfood.model.merchant.MerchantGetIncomeRequest;
import com.synergy.binarfood.model.merchant.MerchantGetRequest;
import com.synergy.binarfood.model.merchant.MerchantIncomeResponse;
import com.synergy.binarfood.model.merchant.MerchantResponse;
import org.springframework.data.domain.Page;

public interface MerchantService {
    public Page<MerchantResponse> findAll(MerchantGetRequest request);
    public MerchantIncomeResponse getIncomeByRange(MerchantGetIncomeRequest request);
}
