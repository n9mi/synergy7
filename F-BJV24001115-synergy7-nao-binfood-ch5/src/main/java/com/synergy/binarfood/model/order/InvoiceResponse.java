package com.synergy.binarfood.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceResponse {
    String totalPrice;
    String username;
    String address;
    String completedAt;
    List<InvoiceItemResponse> items;
}
