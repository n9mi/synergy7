package com.synergy.binfood.service;

import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.order.OrderCreateRequest;
import com.synergy.binfood.util.exception.NotFoundException;
import com.synergy.binfood.util.exception.UnauthorizedException;
import com.synergy.binfood.util.exception.ValidationException;

public interface OrderService {
    public AuthData create(AuthData authData, OrderCreateRequest request) throws UnauthorizedException, NotFoundException, ValidationException;
}
