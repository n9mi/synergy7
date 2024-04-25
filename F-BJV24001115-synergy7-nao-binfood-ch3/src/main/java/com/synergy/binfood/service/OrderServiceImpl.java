package com.synergy.binfood.service;

import com.synergy.binfood.entity.Order;
import com.synergy.binfood.entity.OrderDetail;
import com.synergy.binfood.entity.Product;
import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.order.OrderCreateRequest;
import com.synergy.binfood.model.order.OrderDetailRequest;
import com.synergy.binfood.model.order.OrderDetailResponse;
import com.synergy.binfood.model.order.OrderResponse;
import com.synergy.binfood.repository.*;
import com.synergy.binfood.util.exception.*;
import com.synergy.binfood.util.writer.ReceiptWriter;
import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;
    private final ProductRepository productRepository;

    public List<OrderResponse> findAllOrdersByUserId(AuthData authData) throws UnauthorizedException {
        if (!this.userRepository.isUserExistByIdAndUsername(authData.getUserId(), authData.getUserName())) {
            throw new UnauthorizedException("invalid auth credentials");
        }

        List<Order> userOrders = this.orderRepository.findByUserId(authData.getUserId());
        return Optional.ofNullable(userOrders).orElse(new ArrayList<>())
                .stream()
                .filter(Objects::nonNull)
                .map(e ->
                   new OrderResponse( e.getId(),
                           e.getDestinationAddress(),
                           e.getCreatedAt(),
                           e.isCompleted(),
                           e.getCompletedAt()) ).collect(Collectors.toList());
    }

    public OrderResponse findOrder(AuthData authData, int orderId) throws ValidationException, UnauthorizedException,
            NotFoundException {
        if (!this.userRepository.isUserExistByIdAndUsername(authData.getUserId(), authData.getUserName())) {
            throw new UnauthorizedException("invalid auth credentials");
        }

        if (orderId < 1) {
            throw new ValidationException("invalid order id");
        }

        if (!this.orderRepository.isOwnByUser(authData.getCurrOrderId(), authData.getUserId())) {
            throw new NotFoundException("order doesn't exists");
        }

        Order order = this.orderRepository.findById(orderId);
        return new OrderResponse(order.getId(), order.getDestinationAddress(), order.getCreatedAt(),
                order.isCompleted(), order.getCompletedAt());
    }

    public OrderResponse findOrderWithOrderDetails(AuthData authData, int orderId) throws ValidationException,
            UnauthorizedException, NotFoundException {
        if (!this.userRepository.isUserExistByIdAndUsername(authData.getUserId(), authData.getUserName())) {
            throw new UnauthorizedException("invalid auth credentials");
        }

        if (orderId < 1) {
            throw new ValidationException("invalid order id");
        }

        if (!this.orderRepository.isOwnByUser(orderId, authData.getUserId())) {
            throw new NotFoundException("order doesn't exists");
        }

        Order order = this.orderRepository.findById(orderId);
        List<OrderDetail> orderDetails = this.orderDetailRepository.findAllByOrderId(orderId);

        OrderResponse response = new OrderResponse(order.getId(), order.getDestinationAddress(),
                order.getCreatedAt(), order.isCompleted(), order.getCompletedAt());
        List<OrderDetailResponse> orderDetailsResponses = orderDetails.stream()
                .map(e ->  {
                    Product product = this.productRepository.findById(e.getProductId());
                    return new OrderDetailResponse(e.getId(), product.getProductName(),
                            e.getQuantity(), product.getPrice(), e.getTotalPrice());
                }).collect(Collectors.toList());
        response.setOrderDetails(orderDetailsResponses);

        return response;
    }

    public AuthData createOrder(AuthData authData, OrderCreateRequest request) throws UnauthorizedException, NotFoundException,
            ValidationException {
        if (!this.userRepository.isUserExistByIdAndUsername(authData.getUserId(), authData.getUserName())) {
            throw new UnauthorizedException("invalid auth credentials");
        }

        if (!this.merchantRepository.isExistsAndOpened(authData.getCurrMerchantId())) {
            throw new NotFoundException("merchant doesn't exists");
        }

        Set<ConstraintViolation<OrderCreateRequest>> violations = Service.validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationException(ExceptionUtil.getViolationsMessage(violations));
        }

        Order order = this.orderRepository.create(new Order(authData.getUserId(), request.getDestinationAddress()));
        authData.setCurrOrderId(order.getId());

        return authData;
    }

    public OrderDetailResponse findOrderDetail(AuthData authData, int productId) throws
            UnauthorizedException, NotFoundException {
        if (!this.userRepository.isUserExistByIdAndUsername(authData.getUserId(), authData.getUserName())) {
            throw new UnauthorizedException("invalid auth credentials");
        }

        if (!this.orderDetailRepository.isExistsByOrderAndProductId(authData.getCurrOrderId(), productId)) {
            throw new NotFoundException("order detail doesn't exists");
        }

        OrderDetail orderDetail = this.orderDetailRepository.findByOrderAndProductId(authData.getCurrOrderId(),
            productId);
        Product product = this.productRepository.findById(productId);

        return new OrderDetailResponse(orderDetail.getId(), product.getProductName(), orderDetail.getQuantity(),
                product.getPrice(), orderDetail.getTotalPrice());
    }

    public boolean isOrderDetailExists(AuthData authData, int productId) {
        return this.orderDetailRepository.isExistsByOrderAndProductId(authData.getCurrOrderId(), productId);
    }

    public void createOrderDetail(AuthData authData, OrderDetailRequest request)
            throws UnauthorizedException, ValidationException, NotFoundException, DuplicateItemException {
        if (!this.userRepository.isUserExistByIdAndUsername(authData.getUserId(), authData.getUserName())) {
            throw new UnauthorizedException("invalid auth credentials");
        }

        if (!this.orderRepository.isOwnByUser(authData.getCurrOrderId(), authData.getUserId())) {
            throw new NotFoundException("order doesn't exists");
        }

        Set<ConstraintViolation<OrderDetailRequest>> violations = Service.validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationException(ExceptionUtil.getViolationsMessage(violations));
        }

        if (!this.merchantRepository.isProductExistsOnMerchant(authData.getCurrMerchantId(), request.getProductId())) {
            throw new NotFoundException("product doesn't exists on this merchant");
        }

        if (this.orderDetailRepository.isExistsByOrderAndProductId(authData.getCurrOrderId(), request.getProductId())) {
            throw new DuplicateItemException("product already ordered, please update instead");
        }

        Product product = this.productRepository.findById(request.getProductId());
        this.orderDetailRepository.create(new OrderDetail(
                authData.getCurrOrderId(),
                request.getProductId(),
                request.getQuantity(),
                request.getQuantity() * product.getPrice()));
    }

    public void updateOrderDetail(AuthData authData, OrderDetailRequest request)
            throws UnauthorizedException, ValidationException, NotFoundException {
        if (!this.userRepository.isUserExistByIdAndUsername(authData.getUserId(), authData.getUserName())) {
            throw new UnauthorizedException("invalid auth credentials");
        }

        if (!this.orderRepository.isOwnByUser(authData.getCurrOrderId(), authData.getUserId())) {
            throw new NotFoundException("order doesn't exists");
        }

        Set<ConstraintViolation<OrderDetailRequest>> violations = Service.validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationException(ExceptionUtil.getViolationsMessage(violations));
        }

        if (!this.merchantRepository.isProductExistsOnMerchant(authData.getCurrMerchantId(), request.getProductId())) {
            throw new NotFoundException("product doesn't exists on this merchant");
        }

        if (!this.orderDetailRepository.isExistsByOrderAndProductId(authData.getCurrOrderId(), request.getProductId())) {
            throw new NotFoundException("order detail doesn't exists");
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
        if (!this.userRepository.isUserExistByIdAndUsername(authData.getUserId(), authData.getUserName())) {
            throw new UnauthorizedException("invalid auth credentials");
        }

        if (!this.orderRepository.isOwnByUser(authData.getCurrOrderId(), authData.getUserId())) {
            throw new NotFoundException("order detail doesn't exists");
        }

        if (productId < 1) {
            throw new ValidationException("invalid product id");
        }

        if (!this.orderDetailRepository.isExistsByOrderAndProductId(authData.getCurrOrderId(), productId)) {
            throw new NotFoundException("order not found");
        }

        OrderDetail orderDetail = this.orderDetailRepository.findByOrderAndProductId(authData.getCurrOrderId(), productId);
        this.orderDetailRepository.delete(orderDetail.getId());
    }

    public void finishOrder(AuthData authData) throws UnauthorizedException, ValidationException, NotFoundException,
            WriterException {
        if (!this.userRepository.isUserExistByIdAndUsername(authData.getUserId(), authData.getUserName())) {
            throw new UnauthorizedException("invalid auth credentials");
        }

        if (!this.orderRepository.isOwnByUser(authData.getCurrOrderId(), authData.getUserId())) {
            throw new NotFoundException("order doesn't exists");
        }

        if (this.orderDetailRepository.findAllByOrderId(authData.getCurrOrderId()).isEmpty()) {
            throw new ValidationException("order detail can't be empty");
        }

        Order order = this.orderRepository.findById(authData.getCurrOrderId());
        this.orderRepository.finish(order);

        OrderResponse orderResponse = this.findOrderWithOrderDetails(authData, authData.getCurrOrderId());
        ReceiptWriter.writeToPDF(orderResponse);
    }
}
