package com.synergy.binarfood.service;

import com.synergy.binarfood.entity.Merchant;
import com.synergy.binarfood.entity.Product;
import com.synergy.binarfood.entity.User;
import com.synergy.binarfood.model.product.ProductDeleteRequest;
import com.synergy.binarfood.model.product.ProductGetByMerchantRequest;
import com.synergy.binarfood.model.product.ProductRequest;
import com.synergy.binarfood.model.product.ProductResponse;
import com.synergy.binarfood.repository.MerchantRepository;
import com.synergy.binarfood.repository.ProductRepository;
import com.synergy.binarfood.repository.UserRepository;
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
    private final UserRepository userRepository;
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

    public ProductResponse create(ProductRequest request) {
        this.validationService.validate(request);
        User user = this.userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exists"));
        Merchant merchant = this.merchantRepository
                .findById(UUID.fromString(request.getMerchantId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant doesn't exists"));
        if (!merchant.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant doesn't exists");
        }

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .merchant(merchant)
                .build();
        this.productRepository.save(product);

        return ProductResponse.builder()
                .id(String.valueOf(product.getId()))
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    public ProductResponse update(ProductRequest request) {
        this.validationService.validate(request);
        User user = this.userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exists"));
        Merchant merchant = this.merchantRepository
                .findById(UUID.fromString(request.getMerchantId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant doesn't exists"));
        if (!merchant.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant doesn't exists");
        }

        Product product = this.productRepository
                .findById(UUID.fromString(request.getProductId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product doesn't exists"));
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        this.productRepository.save(product);

        return ProductResponse.builder()
                .id(String.valueOf(product.getId()))
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    public void delete(ProductDeleteRequest request) {
        this.validationService.validate(request);
        User user = this.userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exists"));
        Merchant merchant = this.merchantRepository
                .findById(UUID.fromString(request.getMerchantId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant doesn't exists"));
        if (!merchant.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant doesn't exists");
        }

        Product product = this.productRepository
                .findById(UUID.fromString(request.getProductId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product doesn't exists"));
        this.productRepository.delete(product);
    }
}
