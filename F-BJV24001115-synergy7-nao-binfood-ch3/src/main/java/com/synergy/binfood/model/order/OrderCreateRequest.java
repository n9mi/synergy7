package com.synergy.binfood.model.order;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class OrderCreateRequest {
    @NotBlank(message = "destination address can't be blank")
    private String destinationAddress;
}
