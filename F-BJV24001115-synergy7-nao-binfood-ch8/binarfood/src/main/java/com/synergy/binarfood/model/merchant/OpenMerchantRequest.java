package com.synergy.binarfood.model.merchant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenMerchantRequest {
    @JsonIgnore
    @NotBlank
    String email;

    boolean open;
}
