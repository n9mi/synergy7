package com.synergy.binfood.service;

import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.order.OrderDetailRequest;
import com.synergy.binfood.util.exception.DuplicateItemException;
import com.synergy.binfood.util.exception.NotFoundException;
import com.synergy.binfood.util.exception.UnauthorizedException;
import com.synergy.binfood.util.exception.ValidationException;

public interface OrderDetailService {
    public void create(AuthData authData, OrderDetailRequest request)
            throws UnauthorizedException, ValidationException, NotFoundException, DuplicateItemException;

    public boolean isOrderDetailExists(AuthData authData, int productId);

    public void updateOrderDetail(AuthData authData, OrderDetailRequest request) throws UnauthorizedException,
            ValidationException, NotFoundException;

    public void deleteOrderDetail(AuthData authData, int productId) throws UnauthorizedException,
            NotFoundException, ValidationException;
}
