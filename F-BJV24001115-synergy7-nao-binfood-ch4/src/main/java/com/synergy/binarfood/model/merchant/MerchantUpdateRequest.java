package com.synergy.binarfood.model.merchant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantUpdateRequest {
    @NotBlank
    String id;

    @NotBlank
    @Size(max = 100)
    String name;

    @NotBlank
    @Size(max = 300)
    String location;

    boolean open;
}
