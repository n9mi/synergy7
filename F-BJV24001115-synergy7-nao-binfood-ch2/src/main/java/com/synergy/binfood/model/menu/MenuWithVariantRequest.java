package com.synergy.binfood.model.menu;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuWithVariantRequest {
    @NotBlank(message = "menu code cannot be blank")
    String menuCode;

    // variantCode are used to exclude specific variant
    String variantCode;
}
