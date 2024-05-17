package com.synergy.binarfood.controller.merchant;

import com.synergy.binarfood.model.product.ProductDeleteRequest;
import com.synergy.binarfood.model.product.ProductGetByMerchantRequest;
import com.synergy.binarfood.model.product.ProductRequest;
import com.synergy.binarfood.model.product.ProductResponse;
import com.synergy.binarfood.model.web.WebResponse;
import com.synergy.binarfood.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantProductController {
    private final ProductService productService;

    @GetMapping(
            path = "/merchants/{merchantId}/products",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<ProductResponse>> getProductsByMerchant(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @PathVariable("merchantId") String merchantId) {
        ProductGetByMerchantRequest request = ProductGetByMerchantRequest.builder()
                .page(page)
                .pageSize(pageSize)
                .merchantId(merchantId).build();
        Page<ProductResponse> response = this.productService.findAllByMerchant(request);

        return WebResponse.<List<ProductResponse>>builder().data(response.getContent()).build();
    }


    @PostMapping(
            path = "/merchants/{merchantId}/products",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> create(
            Authentication authentication,
            @PathVariable("merchantId") String merchantId,
            @RequestBody ProductRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        request.setMerchantId(merchantId);
        request.setUsername(userDetails.getUsername());
        ProductResponse product = this.productService.create(request);

        return WebResponse.<ProductResponse>builder()
                .data(product)
                .build();
    }

    @PutMapping(
            path = "/merchants/{merchantId}/products/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> update(
            Authentication authentication,
            @PathVariable("merchantId") String merchantId,
            @PathVariable("productId") String productId,
            @RequestBody ProductRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        request.setMerchantId(merchantId);
        request.setUsername(userDetails.getUsername());
        request.setProductId(productId);
        ProductResponse product = this.productService.update(request);

        return WebResponse.<ProductResponse>builder()
                .data(product)
                .build();
    }

    @DeleteMapping(
            path = "/merchants/{merchantId}/products/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(
            Authentication authentication,
            @PathVariable("merchantId") String merchantId,
            @PathVariable("productId") String productId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        ProductDeleteRequest request = ProductDeleteRequest.builder()
                .merchantId(merchantId)
                .username(userDetails.getUsername())
                .productId(productId)
                .build();

        this.productService.delete(request);

        return WebResponse.<String>builder()
                .data(null)
                .build();
    }
}
