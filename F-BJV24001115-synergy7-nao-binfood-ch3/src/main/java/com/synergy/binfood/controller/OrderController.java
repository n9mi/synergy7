package com.synergy.binfood.controller;

import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.order.OrderCreateRequest;
import com.synergy.binfood.model.order.OrderDetailRequest;
import com.synergy.binfood.service.OrderDetailService;
import com.synergy.binfood.util.exception.DuplicateItemException;
import com.synergy.binfood.util.exception.NotFoundException;
import com.synergy.binfood.util.exception.UnauthorizedException;
import com.synergy.binfood.util.exception.ValidationException;
import lombok.AllArgsConstructor;

import com.synergy.binfood.service.OrderService;

@AllArgsConstructor
public class OrderController  {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    public AuthData create(AuthData authData, String destinationAddress) throws UnauthorizedException, NotFoundException,
            ValidationException {
        return this.orderService.create(authData, new OrderCreateRequest(destinationAddress));
    }

    public void addOrderDetail(AuthData authData, int productId, int quantity)
            throws UnauthorizedException, ValidationException, NotFoundException, DuplicateItemException {
        this.orderDetailService.create(authData, new OrderDetailRequest(productId, quantity));
    }

    public boolean isOrderDetailExists(AuthData authData, int productId) {
        return this.orderDetailService.isOrderDetailExists(authData, productId);
    }

    public void updateOrderDetail(AuthData authData, int productId, int quantity)
            throws UnauthorizedException, ValidationException, NotFoundException {
        this.orderDetailService.updateOrderDetail(authData, new OrderDetailRequest(productId, quantity));
    }

    public void deleteOrderDetail(AuthData authData, int productId) throws UnauthorizedException, ValidationException,
            NotFoundException {
        this.orderDetailService.deleteOrderDetail(authData, productId);
    }
}
