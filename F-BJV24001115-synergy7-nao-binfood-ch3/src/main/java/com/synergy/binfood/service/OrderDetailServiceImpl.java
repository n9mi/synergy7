package com.synergy.binfood.service;

import com.synergy.binfood.entity.Order;
import com.synergy.binfood.entity.OrderDetail;
import com.synergy.binfood.entity.Product;
import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.order.OrderDetailRequest;
import com.synergy.binfood.model.order.OrderDetailResponse;
import com.synergy.binfood.repository.*;
import com.synergy.binfood.util.exception.*;
import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;
    private final ProductRepository productRepository;

    public void create(AuthData authData, OrderDetailRequest request)
            throws UnauthorizedException, ValidationException, NotFoundException, DuplicateItemException {
        if (!this.userRepository.isUserExistByIdAndUsername(authData.getUserId(), authData.getUserName())) {
            throw new UnauthorizedException("invalid auth credentials");
        }

        if (!this.orderRepository.isOwnByUser(authData.getCurrOrderId(), authData.getUserId())) {
            throw new UnauthorizedException("forbidden");
        }

        Set<ConstraintViolation<OrderDetailRequest>> violations = Service.validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationException(ExceptionUtil.getViolationsMessage(violations));
        }

        if (!this.merchantRepository.isProductExistsOnMerchant(authData.getCurrMerchantId(), request.getProductId())) {
            throw new NotFoundException("product not found on this merchant");
        }

        if (this.orderDetailRepository.isExistsByOrderAndProductId(authData.getCurrOrderId(), request.getProductId())) {
            throw new DuplicateItemException("product already ordered, please update instead");
        }

        this.orderDetailRepository.create(new OrderDetail(authData.getCurrOrderId(), request.getProductId(),
                request.getQuantity()));
    }

    public boolean isOrderDetailExists(AuthData authData, int productId) {
        return this.orderDetailRepository.isExistsByOrderAndProductId(authData.getCurrOrderId(), productId);
    }

    public void updateOrderDetail(AuthData authData, OrderDetailRequest request)
            throws UnauthorizedException, ValidationException, NotFoundException {
        if (!this.userRepository.isUserExistByIdAndUsername(authData.getUserId(), authData.getUserName())) {
            throw new UnauthorizedException("invalid auth credentials");
        }

        if (!this.orderRepository.isOwnByUser(authData.getCurrOrderId(), authData.getUserId())) {
            throw new UnauthorizedException("forbidden");
        }

        Set<ConstraintViolation<OrderDetailRequest>> violations = Service.validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationException(ExceptionUtil.getViolationsMessage(violations));
        }

        if (!this.merchantRepository.isProductExistsOnMerchant(authData.getCurrMerchantId(), request.getProductId())) {
            throw new NotFoundException("product not found on this merchant");
        }

        if (!this.orderDetailRepository.isExistsByOrderAndProductId(authData.getCurrOrderId(), request.getProductId())) {
            throw new NotFoundException("order not found");
        }

        OrderDetail orderDetail = this.orderDetailRepository.findByOrderAndProductId(authData.getCurrOrderId(),
                request.getProductId());
        Product product = this.productRepository.findById(request.getProductId());
        this.orderDetailRepository.update(new OrderDetail(
                orderDetail.getId(),
                authData.getCurrOrderId(),
                request.getProductId(),
                request.getQuantity(),
                request.getQuantity() * product.getPrice()));
    }

    public void deleteOrderDetail(AuthData authData, int productId) throws UnauthorizedException,
            NotFoundException, ValidationException {
        if (productId < 1) {
            throw new ValidationException("invalid product id");
        }

        if (!this.userRepository.isUserExistByIdAndUsername(authData.getUserId(), authData.getUserName())) {
            throw new UnauthorizedException("invalid auth credentials");
        }

        if (!this.orderRepository.isOwnByUser(authData.getCurrOrderId(), authData.getUserId())) {
            throw new UnauthorizedException("forbidden");
        }

        if (!this.orderDetailRepository.isExistsByOrderAndProductId(authData.getCurrOrderId(), productId)) {
            throw new NotFoundException("order not found");
        }

        OrderDetail orderDetail = this.orderDetailRepository.findByOrderAndProductId(authData.getCurrOrderId(), productId);
        this.orderDetailRepository.delete(orderDetail.getId());
    }
}
