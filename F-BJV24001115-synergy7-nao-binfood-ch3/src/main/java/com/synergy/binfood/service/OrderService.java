package com.synergy.binfood.service;

import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.order.OrderCreateRequest;
import com.synergy.binfood.model.order.OrderDetailRequest;
import com.synergy.binfood.model.order.OrderDetailResponse;
import com.synergy.binfood.model.order.OrderResponse;
import com.synergy.binfood.util.exception.*;

import java.util.List;

public interface OrderService {
    public List<OrderResponse> findAllOrdersByUserId(AuthData authData) throws UnauthorizedException;

    public OrderResponse findOrder(AuthData authData, int orderId) throws ValidationException, UnauthorizedException,
            NotFoundException;

    public OrderResponse findOrderWithOrderDetails(AuthData authData, int orderId) throws ValidationException,
            UnauthorizedException, NotFoundException;

    public OrderDetailResponse findOrderDetail(AuthData authData, int productId) throws UnauthorizedException,
            NotFoundException;

    public AuthData createOrder(AuthData authData, OrderCreateRequest request) throws UnauthorizedException,
            NotFoundException, ValidationException;

    public void createOrderDetail(AuthData authData, OrderDetailRequest request)
            throws UnauthorizedException, ValidationException, NotFoundException, DuplicateItemException;

    public boolean isOrderDetailExists(AuthData authData, int productId);

    public void updateOrderDetail(AuthData authData, OrderDetailRequest request) throws UnauthorizedException,
            ValidationException, NotFoundException;

    public void deleteOrderDetail(AuthData authData, int productId) throws UnauthorizedException,
            NotFoundException, ValidationException;

    public void finishOrder(AuthData authData) throws UnauthorizedException, ValidationException,
            NotFoundException, WriterException;
}
