package com.synergy.binfood.model.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class MenuWithVariantsResponse {
    private final String menuCode;
    private final String menuName;
    private final int menuPrice;
    private final List<VariantResponse> variantResponses;
}
