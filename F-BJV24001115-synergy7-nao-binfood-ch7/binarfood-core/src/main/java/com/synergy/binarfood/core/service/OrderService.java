package com.synergy.binarfood.core.service;

import com.synergy.binarfood.core.model.order.CheckoutOrderRequest;
import com.synergy.binarfood.core.model.order.GetAllOrderRequest;
import com.synergy.binarfood.core.model.order.OrderRequest;
import com.synergy.binarfood.core.model.order.OrderResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface OrderService {
    public Page<OrderResponse> findAllByUser(GetAllOrderRequest request);
    public OrderResponse findById(UUID orderId, String userEmail);
    public OrderResponse create(OrderRequest request);
    public OrderResponse checkout(CheckoutOrderRequest request);
}
