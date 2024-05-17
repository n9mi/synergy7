package com.synergy.binarfood.controller.buyer;

import com.synergy.binarfood.model.merchant.MerchantGetRequest;
import com.synergy.binarfood.model.merchant.MerchantResponse;
import com.synergy.binarfood.model.web.WebResponse;
import com.synergy.binarfood.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buyer")
@RequiredArgsConstructor
public class BuyerMerchantController {
    private final MerchantService merchantService;

    @GetMapping(
            path = "/merchants",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<MerchantResponse>> getMerchants(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        MerchantGetRequest request = MerchantGetRequest.builder()
                .page(page).pageSize(pageSize).onlyOpen(true).build();
        Page<MerchantResponse> response = this.merchantService.findAll(request);

        return WebResponse.<List<MerchantResponse>>builder().data(response.getContent()).build();
    }
}
