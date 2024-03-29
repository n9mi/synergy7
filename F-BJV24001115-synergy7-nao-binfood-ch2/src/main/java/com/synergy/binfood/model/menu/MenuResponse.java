package com.synergy.binfood.model.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MenuResponse {
    private String menuCode;
    private String menuName;
    private int menuPrice;
}
