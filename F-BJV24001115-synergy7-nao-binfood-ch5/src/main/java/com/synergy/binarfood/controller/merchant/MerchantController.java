package com.synergy.binarfood.controller.merchant;

import com.synergy.binarfood.model.merchant.*;
import com.synergy.binarfood.model.web.WebResponse;
import com.synergy.binarfood.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/merchant/merchants")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantService merchantService;

    @GetMapping("")
    public WebResponse<List<MerchantResponse>> merchants(
            Authentication authentication,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        MerchantGetRequest request = MerchantGetRequest.builder()
                .page(page).pageSize(pageSize).username(userDetails.getUsername()).build();
        Page<MerchantResponse> response = this.merchantService.findAll(request);

        return WebResponse.<List<MerchantResponse>>builder().data(response.getContent()).build();
    }

    @GetMapping("/{merchantId}/monthly-report")
    public WebResponse<MerchantIncomeResponse> monthlyReport(
            Authentication authentication,
            @PathVariable(value = "merchantId") String merchantId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        MerchantGetIncomeRequest request = MerchantGetIncomeRequest.builder()
                .incomeType(IncomeTypeRequest.MONTHLY)
                .username(userDetails.getUsername())
                .merchantId(merchantId)
                .build();
        MerchantIncomeResponse response = this.merchantService.getIncomeByRange(request);

        return WebResponse.<MerchantIncomeResponse>builder().data(response).build();
    }

    @GetMapping("/{merchantId}/weekly-report")
    public WebResponse<MerchantIncomeResponse> weeklyReport(
            Authentication authentication,
            @PathVariable(value = "merchantId") String merchantId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        MerchantGetIncomeRequest request = MerchantGetIncomeRequest.builder()
                .incomeType(IncomeTypeRequest.WEEKLY)
                .username(userDetails.getUsername())
                .merchantId(merchantId)
                .build();
        MerchantIncomeResponse response = this.merchantService.getIncomeByRange(request);

        return WebResponse.<MerchantIncomeResponse>builder().data(response).build();
    }

    @GetMapping("/{merchantId}/custom-report")
    public WebResponse<MerchantIncomeResponse> customReport(
            Authentication authentication,
            @PathVariable(value = "merchantId") String merchantId,
            @RequestParam("fromDate") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate toDate) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        MerchantGetIncomeRequest request = MerchantGetIncomeRequest.builder()
                .incomeType(IncomeTypeRequest.FILTER_BY_DATE)
                .username(userDetails.getUsername())
                .merchantId(merchantId)
                .fromDate(fromDate)
                .toDate(toDate)
                .build();
        MerchantIncomeResponse response = this.merchantService.getIncomeByRange(request);

        return WebResponse.<MerchantIncomeResponse>builder().data(response).build();
    }

    @PostMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<MerchantResponse> create(
            Authentication authentication,
            @RequestBody MerchantRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        request.setUsername(userDetails.getUsername());

        MerchantResponse merchant = this.merchantService.create(request);

        return WebResponse.<MerchantResponse>builder()
                .data(merchant)
                .build();
    }

    @PutMapping(
            path = "/{merchantId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<MerchantResponse> update(
            Authentication authentication,
            @PathVariable("merchantId") String merchantId,
            @RequestBody MerchantRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        request.setUsername(userDetails.getUsername());
        request.setId(merchantId);

        MerchantResponse merchant = this.merchantService.update(request);

        return WebResponse.<MerchantResponse>builder()
                .data(merchant)
                .build();
    }

    @PatchMapping(
            path = "/{merchantId}/open",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> openMerchant(
            Authentication authentication,
            @PathVariable("merchantId") String merchantId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        MerchantUpdateStatusRequest request = MerchantUpdateStatusRequest.builder()
                .username(userDetails.getUsername())
                .merchantId(merchantId)
                .openStatus(MerchantStatus.OPEN)
                .build();
        this.merchantService.updateStatus(request);

        return WebResponse.<String>builder()
                .data(null)
                .build();
    }

    @PatchMapping(
            path = "/{merchantId}/close",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> closeMerchant(
            Authentication authentication,
            @PathVariable("merchantId") String merchantId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        MerchantUpdateStatusRequest request = MerchantUpdateStatusRequest.builder()
                        .username(userDetails.getUsername())
                                .merchantId(merchantId)
                                        .openStatus(MerchantStatus.CLOSE)
                                                .build();
        this.merchantService.updateStatus(request);

        return WebResponse.<String>builder()
                .data(null)
                .build();
    }

    @DeleteMapping(
            path = "/{merchantId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(
            Authentication authentication,
            @PathVariable("merchantId") String merchantId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        MerchantDeleteRequest request = MerchantDeleteRequest.builder()
                        .username(userDetails.getUsername())
                        .merchantId(merchantId)
                                .build();

        this.merchantService.delete(request);

        return WebResponse.<String>builder()
                .data(null)
                .build();
    }
}
