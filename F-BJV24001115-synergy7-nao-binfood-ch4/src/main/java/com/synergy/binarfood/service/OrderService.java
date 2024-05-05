package com.synergy.binarfood.service;

import com.synergy.binarfood.entity.User;
import com.synergy.binarfood.model.order.*;
import org.springframework.data.domain.Page;

public interface OrderService {
    public OrderResponse create(User user, CreateOrderRequest request);
    public Page<OrderResponse> findAllByUser(User user, int page, int pageSize);
    public OrderWithDetailsResponse findDetailsById(User user, String orderId);
    public OrderDetailResponse createOrderDetail(User user, CreateOrderDetailRequest request);
    public OrderDetailResponse updateOrderDetail(User user, UpdateOrderDetailRequest request);
    public void deleteOrderDetail(User user, DeleteOrderDetailRequest request);
    public OrderWithDetailsResponse checkout(User user, String orderId);
}
