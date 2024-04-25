package com.synergy.binfood.service;

import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.merchant.GetMerchantProductRequest;
import com.synergy.binfood.model.merchant.MerchantResponse;
import com.synergy.binfood.model.merchant.MerchantWithProductsResponse;
import com.synergy.binfood.model.product.ProductResponse;
import com.synergy.binfood.util.exception.NotFoundException;
import com.synergy.binfood.util.exception.UnauthorizedException;
import com.synergy.binfood.util.exception.ValidationException;

import java.util.List;

public interface MerchantService {
    public List<MerchantResponse> findOpened();
    public MerchantWithProductsResponse findOpenedMerchantDetailById(int id) throws NotFoundException;
    public AuthData setCurrentMerchant(AuthData authData, int merchantId) throws UnauthorizedException, NotFoundException;
    public ProductResponse findProductInMerchant(AuthData authData, int productId) throws UnauthorizedException,
            NotFoundException;
}
