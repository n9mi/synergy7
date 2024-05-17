package com.synergy.binarfood.model.merchant;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantUpdateStatusRequest {
    @NotBlank
    String merchantId;

    @NotBlank
    String username;

    @Enumerated(EnumType.STRING)
    MerchantStatus openStatus;
}
