package com.synergy.binfood.controller;

import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.merchant.MerchantResponse;
import com.synergy.binfood.model.merchant.MerchantWithProductsResponse;
import com.synergy.binfood.service.MerchantService;
import com.synergy.binfood.util.exception.NotFoundException;
import com.synergy.binfood.util.exception.UnauthorizedException;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class MerchantController {
    private final MerchantService merchantService;

    public List<MerchantResponse> getOpenedMerchants() {
        return this.merchantService.findOpened();
    }

    public MerchantWithProductsResponse getOpenedMerchantDetailById(int id) throws NotFoundException {
        return this.merchantService.findOpenedMerchantDetailById(id);
    }

    public AuthData setCurrentMerchant(AuthData authData, int merchantId) throws UnauthorizedException, NotFoundException {
        return this.merchantService.setCurrentMerchant(authData, merchantId);
    }
}
