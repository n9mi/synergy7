package com.synergy.binarfood.service;

import com.synergy.binarfood.model.order.*;
import org.springframework.data.domain.Page;

public interface OrderService {
    public OrderResponse create(OrderCreateRequest request);
    public OrderDetailResponse createOrderDetail(OrderDetailRequest request);
    public OrderDetailResponse updateOrderDetail(OrderDetailRequest request);
    public void deleteOrderDetail(OrderDetailDeleteRequest request);
    public InvoiceResponse checkoutOrder(OrderCheckoutRequest request);
    public Page<OrderResponse> findAllByUser(OrderGetRequest request);
    public Page<OrderDetailResponse> findOrderDetailsByOrder(OrderDetailGetRequest request);
}
