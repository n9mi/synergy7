package com.synergy.binarfood.model.merchant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantIncomeResponse {
    private String merchantId;
    private String merchantName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private double totalIncomeWithinRange;
    private List<ProductPerMerchantResponse> products;
}
