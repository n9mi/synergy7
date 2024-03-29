package com.synergy.binfood.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CheckIsOrderItemExistsResponse {
    private boolean isOrderItemExists;
}
