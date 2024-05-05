package com.synergy.binarfood.model.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateRequest {
    @NotBlank
    private String id;

    @NotBlank
    private String merchantId;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Positive
    private double price;
}
