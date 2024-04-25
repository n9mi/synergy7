package com.synergy.binfood.controller;

import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.order.OrderCreateRequest;
import com.synergy.binfood.model.order.OrderDetailRequest;
import com.synergy.binfood.model.order.OrderDetailResponse;
import com.synergy.binfood.model.order.OrderResponse;
import com.synergy.binfood.util.exception.*;
import lombok.AllArgsConstructor;

import com.synergy.binfood.service.OrderService;

import java.util.List;

@AllArgsConstructor
public class OrderController  {
    private final OrderService orderService;

    public List<OrderResponse> getUserOrders(AuthData authData) throws UnauthorizedException {
        return this.orderService.findAllOrdersByUserId(authData);
    }

    public OrderResponse getOrder(AuthData authData, int orderId) throws ValidationException, UnauthorizedException,
            NotFoundException {
        return this.orderService.findOrder(authData, orderId);
    }

    public OrderResponse getOrderWithOrderDetails(AuthData authData, int orderId) throws ValidationException,
            UnauthorizedException, NotFoundException {
        return this.orderService.findOrderWithOrderDetails(authData, orderId);
    }

    public AuthData createOrder(AuthData authData, String destinationAddress) throws UnauthorizedException, NotFoundException,
            ValidationException {
        return this.orderService.createOrder(authData, new OrderCreateRequest(destinationAddress));
    }

    public OrderDetailResponse getOrderDetail(AuthData authData, int productId) throws UnauthorizedException,
            NotFoundException {
        return this.orderService.findOrderDetail(authData, productId);
    }

    public void addOrderDetail(AuthData authData, int productId, int quantity)
            throws UnauthorizedException, ValidationException, NotFoundException, DuplicateItemException {
        this.orderService.createOrderDetail(authData, new OrderDetailRequest(productId, quantity));
    }

    public boolean isOrderDetailExists(AuthData authData, int productId) {
        return this.orderService.isOrderDetailExists(authData, productId);
    }

    public void updateOrderDetail(AuthData authData, int productId, int quantity)
            throws UnauthorizedException, ValidationException, NotFoundException {
        this.orderService.updateOrderDetail(authData, new OrderDetailRequest(productId, quantity));
    }

    public void deleteOrderDetail(AuthData authData, int productId) throws UnauthorizedException, ValidationException,
            NotFoundException {
        this.orderService.deleteOrderDetail(authData, productId);
    }

    public void checkoutCurrentOrder(AuthData authData) throws UnauthorizedException, ValidationException,
            NotFoundException, WriterException {
        this.orderService.finishOrder(authData);
    }
}
