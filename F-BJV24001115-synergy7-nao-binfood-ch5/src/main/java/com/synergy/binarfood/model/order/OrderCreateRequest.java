package com.synergy.binarfood.model.order;

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
public class OrderCreateRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Size(max = 300)
    private String destinationAddress;
}
