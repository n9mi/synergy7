package com.synergy.binarfood.controller.merchant;

import com.synergy.binarfood.model.merchant.*;
import com.synergy.binarfood.model.web.WebResponse;
import com.synergy.binarfood.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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
}
