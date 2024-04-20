package com.synergy.binfood.service;

import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.merchant.MerchantResponse;
import com.synergy.binfood.model.merchant.MerchantWithProductsResponse;
import com.synergy.binfood.util.exception.NotFoundException;
import com.synergy.binfood.util.exception.UnauthorizedException;

import java.util.List;

public interface MerchantService {
    public List<MerchantResponse> findOpened();
    public MerchantWithProductsResponse findOpenedMerchantDetailById(int id) throws NotFoundException;
    public AuthData setCurrentMerchant(AuthData authData, int merchantId) throws UnauthorizedException, NotFoundException;
}
