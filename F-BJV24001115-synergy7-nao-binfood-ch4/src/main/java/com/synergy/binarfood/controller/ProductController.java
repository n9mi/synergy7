package com.synergy.binarfood.controller;

import com.synergy.binarfood.model.DataResponse;
import com.synergy.binarfood.model.product.ProductCreateRequest;
import com.synergy.binarfood.model.product.ProductResponse;
import com.synergy.binarfood.model.product.ProductUpdateRequest;
import com.synergy.binarfood.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(
            path = "/api/v1/products",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<List<ProductResponse>> getAll(@RequestParam(value = "merchantId", required = false, defaultValue = "") String merchantId,
                                                            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        Page<ProductResponse> response = this.productService.findAll(page, pageSize, merchantId);

        return DataResponse.<List<ProductResponse>>builder()
                .data(response.getContent())
                .build();
    }

    @PostMapping(
            path = "/api/v1/products",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<ProductResponse> create(@RequestBody ProductCreateRequest request) {
        ProductResponse product = this.productService.create(request);

        return DataResponse.<ProductResponse>builder()
                .data(product)
                .build();
    }

    @PutMapping(
            path = "/api/v1/products/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<ProductResponse> update(@PathVariable("id") String id,
                                                @RequestBody ProductUpdateRequest request) {
        request.setId(id);
        ProductResponse product = this.productService.update(request);

        return DataResponse.<ProductResponse>builder()
                .data(product)
                .build();
    }

    @DeleteMapping(
            path = "/api/v1/products/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<String> delete(@PathVariable("id") String id) {
        this.productService.delete(id);

        return DataResponse.<String>builder()
                .data("")
                .build();
    }
}
