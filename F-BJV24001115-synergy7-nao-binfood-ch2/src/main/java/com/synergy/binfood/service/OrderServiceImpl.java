package com.synergy.binfood.service;

import com.synergy.binfood.entity.Menu;
import com.synergy.binfood.entity.Order;
import com.synergy.binfood.entity.OrderItem;
import com.synergy.binfood.entity.Variant;
import com.synergy.binfood.model.order.*;
import com.synergy.binfood.repository.MenuRepository;
import com.synergy.binfood.repository.OrderItemRepository;
import com.synergy.binfood.repository.OrderRepository;
import com.synergy.binfood.repository.VariantRepository;
import com.synergy.binfood.utils.ExceptionUtil;
import com.synergy.binfood.utils.exception.DuplicateItemError;
import com.synergy.binfood.utils.exception.NotFoundError;
import com.synergy.binfood.utils.exception.ValidationError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OrderServiceImpl extends Service implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final VariantRepository variantRepository;
    private final MenuRepository menuRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                            VariantRepository variantRepository, MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.variantRepository = variantRepository;
        this.menuRepository = menuRepository;
    }

    public boolean isOrderExists(GetOrderRequest request) {
        return this.orderRepository.isExists(request.getOrderId());
    }

    public OrderResponse find(GetOrderRequest request) throws ValidationError, NotFoundError {
        Set<ConstraintViolation<GetOrderRequest>> violations = Service.validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationError(ExceptionUtil.getViolationsMessage(violations));
        }

        if (!this.orderRepository.isExists(request.getOrderId())) {
            throw new NotFoundError(String.format("order with id %s is not exists", request.getOrderId()));
        }

        Order order = this.orderRepository.find(request.getOrderId());
        List<OrderItem> orderItems = this.orderRepository.findOrderItemsFromOrder(order);
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        int totalPrice = 0;
        for (OrderItem orderItem: orderItems) {
            Menu menu = this.menuRepository.findByCode(orderItem.getMenuCode());
            Variant variant = this.variantRepository.findByCode(orderItem.getVariantCode());
            orderItemResponses.add(new OrderItemResponse(menu.getCode(), menu.getName(), variant.getCode(),
                    variant.getName(), orderItem.getQuantity()));
            totalPrice += menu.getPrice() * orderItem.getQuantity();
        }

        return new OrderResponse(order.getId(), order.getPayAt(), orderItemResponses, totalPrice);
    }

    public OrderIdResponse create() {
        Order order = this.orderRepository.create(new Order());

        return new OrderIdResponse(order.getId());
    }

    public void pay(GetOrderRequest request) throws ValidationError, NotFoundError {
        Set<ConstraintViolation<GetOrderRequest>> violations = Service.validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationError(ExceptionUtil.getViolationsMessage(violations));
        }

        if (!this.orderRepository.isExists(request.getOrderId())) {
            throw new NotFoundError(String.format("order with id %s is not exists", request.getOrderId()));
        }

        Order order = this.orderRepository.find(request.getOrderId());
        this.orderRepository.pay(order);
    }

    private NotFoundError checkOrderItemValid(OrderItemRequest request)  {
        if (!this.orderRepository.isExists(request.getOrderId())) {
            return new NotFoundError("order is not exists");
        }
        if (!this.menuRepository.isExistsByCode(request.getMenuCode())) {
            return new NotFoundError("menu is not exists");
        }

        if (this.menuRepository.isMenuHasAnyVariant(request.getMenuCode())) {
            if (!this.variantRepository.isExistsByCode(request.getVariantCode())) {
                return new NotFoundError("variant is not exists");
            }
            if (!this.menuRepository.isVariantExistsOnMenu(request.getMenuCode(), request.getVariantCode())) {
                return new NotFoundError("variant not available in this menu");
            }
        }

        return null;
    }

    public CheckIsOrderItemExistsResponse checkIsOrderItemOnOrderExists(OrderItemRequest request) {
        return new CheckIsOrderItemExistsResponse(this.orderRepository.isOrderItemExistsOnOrder(request.getOrderId(),
                request.getMenuCode(), request.getVariantCode()));
    }

    public OrderItemResponse findOrderItemFromOrder(OrderItemRequest request) throws NotFoundError {
        if (!this.orderRepository.isOrderItemExistsOnOrder(request.getOrderId(), request.getMenuCode(),
                request.getVariantCode())) {
            throw new NotFoundError("order item is not exists");
        }
        if (!this.menuRepository.isExistsByCode(request.getMenuCode())) {
            throw new NotFoundError("menu is not exists");
        }
        if ((this.menuRepository.isMenuHasAnyVariant(request.getMenuCode())) &&
                (!this.variantRepository.isExistsByCode(request.getVariantCode()))) {
            throw new NotFoundError("variant is not exists");
        }

        OrderItem orderItem = this.orderItemRepository.find(request.getOrderId(), request.getMenuCode(), request.getVariantCode());
        Menu menu = this.menuRepository.findByCode(orderItem.getMenuCode());
        Variant variant = this.variantRepository.findByCode(orderItem.getVariantCode());
        return new OrderItemResponse(menu.getCode(), menu.getName(), variant.getCode(),
                variant.getName(), orderItem.getQuantity());
    }

    public void createOrderItem(OrderItemRequest request) throws ValidationError, NotFoundError, DuplicateItemError {
        Set<ConstraintViolation<OrderItemRequest>> violations = Service.validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationError(ExceptionUtil.getViolationsMessage(violations));
        }

        NotFoundError errOrderItem = this.checkOrderItemValid(request);
        if (errOrderItem != null) {
            throw errOrderItem;
        }

        if (this.orderRepository.isOrderItemExistsOnOrder(request.getOrderId(), request.getMenuCode(),
                request.getVariantCode())) {
            throw new DuplicateItemError("menu already ordered. edit instead");
        }

        this.orderItemRepository.create(new OrderItem(request.getOrderId(), request.getMenuCode(),
                request.getVariantCode(), request.getQuantity()));
    }

    public void updateOrderItem(OrderItemRequest request) throws ValidationError, NotFoundError {
        Set<ConstraintViolation<OrderItemRequest>> violations = Service.validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationError(ExceptionUtil.getViolationsMessage(violations));
        }

        NotFoundError errOrderItem = this.checkOrderItemValid(request);
        if (errOrderItem != null) {
            throw errOrderItem;
        }

        if (!this.orderRepository.isOrderItemExistsOnOrder(request.getOrderId(), request.getMenuCode(),
                request.getVariantCode())) {
            throw new NotFoundError("menu not ordered. create instead");
        }

        this.orderItemRepository.update(new OrderItem(request.getOrderId(), request.getMenuCode(),
                request.getVariantCode(), request.getQuantity()));
    }

    public void deleteOrderItem(DeleteOrderItemRequest request) throws ValidationError, NotFoundError {
        Set<ConstraintViolation<DeleteOrderItemRequest>> violations = Service.validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationError(ExceptionUtil.getViolationsMessage(violations));
        }

        if ((!this.orderRepository.isExists(request.getOrderId())) || (!this.orderRepository.
                isOrderItemExistsOnOrder(request.getOrderId(), request.getMenuCode(), request.getVariantCode()))) {
            throw new NotFoundError("order with id %s is not exists");
        }

        this.orderItemRepository.delete(new OrderItem(request.getOrderId(), request.getMenuCode(),
                request.getVariantCode(), 0));
    }
}
