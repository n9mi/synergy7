package com.synergy.binfood.service;

import com.synergy.binfood.entity.Order;
import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.order.OrderCreateRequest;
import com.synergy.binfood.repository.MerchantRepository;
import com.synergy.binfood.repository.UserRepository;
import com.synergy.binfood.util.exception.ExceptionUtil;
import com.synergy.binfood.util.exception.NotFoundException;
import com.synergy.binfood.util.exception.UnauthorizedException;
import com.synergy.binfood.util.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;

import com.synergy.binfood.repository.OrderRepository;

import java.util.Set;

@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;

    public AuthData create(AuthData authData, OrderCreateRequest request) throws UnauthorizedException, NotFoundException, ValidationException {
        if (!this.userRepository.isUserExistByIdAndUsername(authData.getUserId(), authData.getUserName())) {
            throw new UnauthorizedException("invalid auth credentials");
        }

        if (!this.merchantRepository.isExistsAndOpened(authData.getCurrMerchantId())) {
            throw new NotFoundException("merchant not found");
        }

        Set<ConstraintViolation<OrderCreateRequest>> violations = Service.validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationException(ExceptionUtil.getViolationsMessage(violations));
        }

        Order order = this.orderRepository.create(new Order(authData.getUserId(), request.getDestinationAddress()));
        authData.setCurrOrderId(order.getId());

        return authData;
    }
}
