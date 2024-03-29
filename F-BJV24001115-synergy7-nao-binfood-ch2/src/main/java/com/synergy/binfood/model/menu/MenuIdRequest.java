package com.synergy.binfood.model.menu;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MenuIdRequest {
    @NotBlank(message = "menu code cannot be blank")
    private String menuCode;
}
