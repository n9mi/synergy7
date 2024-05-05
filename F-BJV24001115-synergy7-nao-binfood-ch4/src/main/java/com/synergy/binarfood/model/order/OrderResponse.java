package com.synergy.binarfood.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    String id;
    Date orderAt;
    String destinationAddress;
    Date completedAt;
}
