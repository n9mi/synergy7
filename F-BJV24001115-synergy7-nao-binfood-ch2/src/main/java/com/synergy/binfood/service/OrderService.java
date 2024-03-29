package com.synergy.binfood.service;

import com.synergy.binfood.model.order.*;
import com.synergy.binfood.utils.exception.DuplicateItemError;
import com.synergy.binfood.utils.exception.NotFoundError;
import com.synergy.binfood.utils.exception.ReceiptWriterError;
import com.synergy.binfood.utils.exception.ValidationError;

public interface OrderService {
    OrderResponse find(GetOrderRequest request) throws ValidationError, NotFoundError;
    OrderIdResponse create();
    void pay(GetOrderRequest request) throws ValidationError, NotFoundError, ReceiptWriterError;
    CheckIsOrderItemExistsResponse checkIsOrderItemOnOrderExists(OrderItemRequest request);
    OrderItemResponse findOrderItemFromOrder(OrderItemRequest request) throws NotFoundError;
    void createOrderItem(OrderItemRequest request) throws ValidationError, NotFoundError, DuplicateItemError;
    void updateOrderItem(OrderItemRequest request) throws ValidationError, NotFoundError;
    void deleteOrderItem(DeleteOrderItemRequest request) throws ValidationError, NotFoundError;
}
