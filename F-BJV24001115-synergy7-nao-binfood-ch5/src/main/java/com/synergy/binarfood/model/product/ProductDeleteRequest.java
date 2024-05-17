package com.synergy.binarfood.model.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class ProductDeleteRequest {
    @NotBlank
    String merchantId;

    @NotBlank
    String productId;

    @NotBlank
    String username;
}
