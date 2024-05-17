package com.synergy.binarfood.service;

import com.synergy.binarfood.entity.Merchant;
import com.synergy.binarfood.entity.Product;
import com.synergy.binarfood.model.product.ProductGetByMerchantRequest;
import com.synergy.binarfood.model.product.ProductResponse;
import com.synergy.binarfood.repository.MerchantRepository;
import com.synergy.binarfood.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;
    private final ValidationService validationService;

    public Page<ProductResponse> findAllByMerchant(ProductGetByMerchantRequest request) {
        Merchant merchant = this.merchantRepository.findById(UUID.fromString(request.getMerchantId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant doesn't exists"));

        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize());
        Page<Product> products = this.productRepository.findAllByMerchant(merchant, pageable);

        return products.map(new Function<Product, ProductResponse>() {
            @Override
            public ProductResponse apply(Product product) {
                return ProductResponse.builder()
                        .id(product.getId().toString())
                        .name(product.getName())
                        .price(product.getPrice())
                        .build();
            }
        });
    }


}
