package com.synergy.binarfood.controller;

import com.synergy.binarfood.entity.User;
import com.synergy.binarfood.model.DataResponse;
import com.synergy.binarfood.model.merchant.MerchantCreateRequest;
import com.synergy.binarfood.model.merchant.MerchantResponse;
import com.synergy.binarfood.model.merchant.MerchantUpdateRequest;
import com.synergy.binarfood.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MerchantController {

    @Autowired
    MerchantService merchantService;

    @GetMapping(
            path = "/api/v1/merchants",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<List<MerchantResponse>> getAll(User user,
                                                            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        Page<MerchantResponse> response = this.merchantService.findAll(page, pageSize);

        return DataResponse.<List<MerchantResponse>>builder()
                .data(response.getContent())
                .build();
    }

    @GetMapping(
            path = "/api/v1/merchants/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<MerchantResponse> getById(@PathVariable("id") String id) {
        MerchantResponse merchant = this.merchantService.findById(id);

        return DataResponse.<MerchantResponse>builder()
                .data(merchant)
                .build();
    }

    @PostMapping(
            path = "/api/v1/merchants",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<MerchantResponse> create(@RequestBody MerchantCreateRequest request) {
        MerchantResponse merchant = this.merchantService.create(request);

        return DataResponse.<MerchantResponse>builder()
                .data(merchant)
                .build();
    }

    @PutMapping(
            path = "/api/v1/merchants/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<MerchantResponse> update(@PathVariable("id") String id,
                                                 @RequestBody MerchantUpdateRequest request) {
        request.setId(id);
        MerchantResponse merchant = this.merchantService.update(request);

        return DataResponse.<MerchantResponse>builder()
                .data(merchant)
                .build();
    }

    @DeleteMapping(
            path = "/api/v1/merchants/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<String> delete(@PathVariable("id") String id) {
        this.merchantService.delete(id);

        return DataResponse.<String>builder().data("").build();
    }
}
