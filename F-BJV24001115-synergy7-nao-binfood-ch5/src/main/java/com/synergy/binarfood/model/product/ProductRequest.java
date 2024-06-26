package com.synergy.binarfood.model.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class ProductRequest {
    @NotBlank
    String name;

    @Positive
    double price;

    @NotBlank
    String merchantId;

    String productId;

    @NotBlank
    String username;
}
