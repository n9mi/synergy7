package com.synergy.binfood.service;

import com.synergy.binfood.entity.Merchant;
import com.synergy.binfood.entity.Product;
import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.merchant.GetMerchantProductRequest;
import com.synergy.binfood.model.merchant.MerchantResponse;
import com.synergy.binfood.model.merchant.MerchantWithProductsResponse;
import com.synergy.binfood.model.product.ProductResponse;
import com.synergy.binfood.repository.MerchantRepository;
import com.synergy.binfood.repository.ProductRepository;
import com.synergy.binfood.repository.UserRepository;
import com.synergy.binfood.util.exception.ExceptionUtil;
import com.synergy.binfood.util.exception.NotFoundException;
import com.synergy.binfood.util.exception.UnauthorizedException;
import com.synergy.binfood.util.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MerchantServiceImpl implements MerchantService {
    private final MerchantRepository merchantRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<MerchantResponse> findOpened() {
        return this.merchantRepository.findOpened().stream()
                .map(merchantEntity -> new MerchantResponse(
                        merchantEntity.getId(),
                        merchantEntity.getMerchantName(),
                        merchantEntity.getMerchantLocation(),
                        merchantEntity.isOpen()
                )).collect(Collectors.toList());
    }

    public MerchantWithProductsResponse findOpenedMerchantDetailById(int id) throws NotFoundException {
        if (!this.merchantRepository.isExistsAndOpened(id)) {
            throw new NotFoundException("merchant not found");
        }

        Merchant merchant = this.merchantRepository.findById(id);
        List<Product> merchantProducts = this.merchantRepository.findProducts(id);

        List<ProductResponse> productResponses = merchantProducts.stream().map(productEntity ->
                new ProductResponse(productEntity.getId(),
                        productEntity.getProductName(),
                        productEntity.getPrice())).collect(Collectors.toList());

        return new MerchantWithProductsResponse(merchant.getId(),
                merchant.getMerchantName(),
                merchant.getMerchantLocation(),
                merchant.isOpen(),
                productResponses);
    }

    public AuthData setCurrentMerchant(AuthData authData, int merchantId) throws UnauthorizedException, NotFoundException {
        if (!this.userRepository.isUserExistByIdAndUsername(authData.getUserId(), authData.getUserName())) {
            throw new UnauthorizedException("invalid auth credentials");
        }

        if (!this.merchantRepository.isExistsAndOpened(merchantId)) {
            throw new NotFoundException("merchant not found");
        }

        authData.setCurrMerchantId(merchantId);
        return authData;
    }

    public ProductResponse findProductInMerchant(AuthData authData, int productId) throws UnauthorizedException,
            NotFoundException {
        if (!this.userRepository.isUserExistByIdAndUsername(authData.getUserId(), authData.getUserName())) {
            throw new UnauthorizedException("invalid auth credentials");
        }

        if (!this.merchantRepository.isProductExistsOnMerchant(authData.getCurrMerchantId(), productId)) {
            throw new NotFoundException("product doesn't exists on this merchant");
        }

        Product product = productRepository.findById(productId);
        return new ProductResponse(product.getId(), product.getProductName(), product.getPrice());
    }
}
