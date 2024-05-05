package com.synergy.binarfood.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderWithDetailsResponse {
    String id;
    Date orderAt;
    String destinationAddress;
    Date completedAt;
    List<OrderDetailResponse> orderDetails;
    Double totalPrice;
}
