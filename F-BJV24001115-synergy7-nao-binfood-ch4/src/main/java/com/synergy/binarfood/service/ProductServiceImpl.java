package com.synergy.binarfood.service;

import com.synergy.binarfood.entity.Merchant;
import com.synergy.binarfood.entity.Product;
import com.synergy.binarfood.model.merchant.MerchantResponse;
import com.synergy.binarfood.model.product.ProductCreateRequest;
import com.synergy.binarfood.model.product.ProductResponse;
import com.synergy.binarfood.model.product.ProductUpdateRequest;
import com.synergy.binarfood.repository.MerchantRepository;
import com.synergy.binarfood.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.function.Function;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    ValidationService validationService;

    public Page<ProductResponse> findAll(int page, int pageSize, String merchantId) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<ProductResponse> products;

        if (!merchantId.isEmpty()) {
            Merchant merchant = this.merchantRepository.findById(UUID.fromString(merchantId))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant not found"));

            Page<Product> results; results = this.productRepository.findAllByMerchant(merchant, pageable);
            products = results.map(new Function<Product, ProductResponse>() {
                @Override
                public ProductResponse apply(Product product) {
                    return ProductResponse.builder()
                            .id(String.valueOf(product.getId()))
                            .name(product.getName())
                            .price(product.getPrice())
                            .build();
                }
            });
        } else {
            Page<Product> results; results = this.productRepository.findAll(pageable);

            products = results.map(new Function<Product, ProductResponse>() {
                @Override
                public ProductResponse apply(Product product) {
                    Merchant merchant = product.getMerchant();
                    MerchantResponse merchantResponse = MerchantResponse.builder()
                            .id(String.valueOf(merchant.getId()))
                            .name(merchant.getName())
                            .location(merchant.getLocation())
                            .open(merchant.isOpen())
                            .build();

                    return ProductResponse.builder()
                            .id(String.valueOf(product.getId()))
                            .name(product.getName())
                            .price(product.getPrice())
                            .merchant(merchantResponse)
                            .build();
                }
            });
        }

        return products;
    }

    public ProductResponse create(ProductCreateRequest request) {
        this.validationService.validate(request);

        Merchant merchant = this.merchantRepository.findById(UUID.fromString(request.getMerchantId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant not found"));

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .merchant(merchant)
                .build();
        this.productRepository.save(product);

        MerchantResponse merchantResponse = MerchantResponse.builder()
                .id(String.valueOf(merchant.getId()))
                .name(merchant.getName())
                .location(merchant.getLocation())
                .open(merchant.isOpen())
                .build();
        return ProductResponse.builder()
                .id(String.valueOf(product.getId()))
                .name(product.getName())
                .price(product.getPrice())
                .merchant(merchantResponse)
                .build();
    }

    public ProductResponse update(ProductUpdateRequest request) {
        this.validationService.validate(request);

        Product product = this.productRepository.findById(UUID.fromString(request.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
        Merchant merchant = this.merchantRepository.findById(UUID.fromString(request.getMerchantId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant not found"));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setMerchant(merchant);
        this.productRepository.save(product);

        MerchantResponse merchantResponse = MerchantResponse.builder()
                .id(String.valueOf(merchant.getId()))
                .name(merchant.getName())
                .location(merchant.getLocation())
                .open(merchant.isOpen())
                .build();
        return ProductResponse.builder()
                .id(String.valueOf(product.getId()))
                .name(product.getName())
                .price(product.getPrice())
                .merchant(merchantResponse)
                .build();
    }

    public void delete(String id) {
        Product product = this.productRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));

        this.productRepository.delete(product);
    }
}
