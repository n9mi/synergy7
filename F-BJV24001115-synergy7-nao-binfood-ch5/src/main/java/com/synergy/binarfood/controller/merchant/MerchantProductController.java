package com.synergy.binarfood.controller.merchant;

import com.synergy.binarfood.model.product.ProductGetByMerchantRequest;
import com.synergy.binarfood.model.product.ProductResponse;
import com.synergy.binarfood.model.web.WebResponse;
import com.synergy.binarfood.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
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
}
