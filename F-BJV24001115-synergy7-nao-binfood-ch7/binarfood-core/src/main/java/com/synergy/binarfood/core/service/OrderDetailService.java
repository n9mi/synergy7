package com.synergy.binarfood.core.service;

import com.synergy.binarfood.core.model.order.DeleteOrderDetailRequest;
import com.synergy.binarfood.core.model.order.OrderDetailRequest;
import com.synergy.binarfood.core.model.order.OrderDetailResponse;
import com.synergy.binarfood.core.model.order.UpdateOrderDetailRequest;

import java.util.UUID;

public interface OrderDetailService {
    public OrderDetailResponse create(OrderDetailRequest request);
    public OrderDetailResponse update(UUID orderDetailId, UpdateOrderDetailRequest request);
    public void delete(UUID orderDetailId, DeleteOrderDetailRequest request);
}
