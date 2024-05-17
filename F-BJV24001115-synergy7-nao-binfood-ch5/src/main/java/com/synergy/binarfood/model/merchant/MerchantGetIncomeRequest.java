package com.synergy.binarfood.model.merchant;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantGetIncomeRequest {
    @Enumerated(EnumType.STRING)
    IncomeTypeRequest incomeType;

    LocalDate fromDate;
    LocalDate toDate;

    String merchantId;
    String username;
}
