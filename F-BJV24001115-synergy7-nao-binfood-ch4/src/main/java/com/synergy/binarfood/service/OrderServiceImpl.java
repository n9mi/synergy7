package com.synergy.binarfood.service;

import com.synergy.binarfood.entity.Order;
import com.synergy.binarfood.entity.OrderDetail;
import com.synergy.binarfood.entity.Product;
import com.synergy.binarfood.entity.User;
import com.synergy.binarfood.model.order.*;
import com.synergy.binarfood.repository.OrderDetailRepository;
import com.synergy.binarfood.repository.OrderRepository;
import com.synergy.binarfood.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    ValidationService validationService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Transactional
    public OrderResponse create(User user, CreateOrderRequest request) {
        this.validationService.validate(request);

        Order order = Order.builder()
                .orderAt(new Date())
                .destinationAddress(request.getDestinationAddress())
                .user(user)
                .build();

        this.orderRepository.save(order);

        return OrderResponse.builder()
                .id(order.getId().toString())
                .orderAt(order.getOrderAt())
                .destinationAddress(order.getDestinationAddress())
                .build();
    }

    public Page<OrderResponse> findAllByUser(User user, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Order> results = this.orderRepository.findAllByUserId(pageable, user.getId());
        Page<OrderResponse> orders = results.map(new Function<Order, OrderResponse>() {
            @Override
            public OrderResponse apply(Order order) {
                return OrderResponse.builder()
                        .id(String.valueOf(order.getId()))
                        .orderAt(order.getOrderAt())
                        .destinationAddress(order.getDestinationAddress())
                        .completedAt(order.getCompletedAt())
                        .build();
            }
        });

        return orders;
    }

    public OrderWithDetailsResponse findDetailsById(User user, String orderId) {
        Order order = this.orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists"));

        if (!user.getId().equals(order.getUser().getId())) {
            log.debug("user with id {} attempting to access order with id {}", user.getId(), order.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists");
        }

        return OrderWithDetailsResponse.builder()
                .id(String.valueOf(order.getId()))
                .orderAt(order.getOrderAt())
                .destinationAddress(order.getDestinationAddress())
                .completedAt(order.getCompletedAt())
                .totalPrice(order.getOrderDetails().stream().map(OrderDetail::getTotalPrice)
                                .reduce(0.0, Double::sum))
                .orderDetails(order.getOrderDetails().stream().map(e ->
                        OrderDetailResponse.builder()
                                .orderId(String.valueOf(e.getOrder().getId()))
                                .orderDetailId(String.valueOf(e.getId()))
                                .merchantId(String.valueOf(e.getProduct().getMerchant().getId()))
                                .merchantName(e.getProduct().getMerchant().getName())
                                .productId(String.valueOf(e.getProduct().getId()))
                                .productName(e.getProduct().getName())
                                .quantity(e.getQuantity())
                                .totalPrice(e.getTotalPrice())
                                .build()
                ).toList()).build();
    }

    @Transactional
    public OrderDetailResponse createOrderDetail(User user, CreateOrderDetailRequest request) {
        this.validationService.validate(request);

        Order order = this.orderRepository.findById(UUID.fromString(request.getOrderId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists"));

        if (!order.getUser().getId().equals(user.getId())) {
            log.debug("user with id {} attempting to access order with id {}", user.getId(), order.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists");
        }

        if (order.getCompletedAt() != null) {
            log.debug("order with id {} already completed at {}", order.getId(), order.getCompletedAt());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "order already completed");
        }

        Product product = this.productRepository.findById(UUID.fromString(request.getProductId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product doesn't exists"));

        if (!product.getMerchant().isOpen()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product unavailable because merchant is closed");
        }

        if (this.orderDetailRepository.existsByOrderAndProduct(order, product)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "order with product " +
                    product.getName() + " already exists, update instead");
        }

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .quantity(request.getQuantity())
                .totalPrice(request.getQuantity() * product.getPrice())
                .build();
        this.orderDetailRepository.save(orderDetail);

        return OrderDetailResponse.builder()
                .orderId(String.valueOf(order.getId()))
                .orderDetailId(String.valueOf(orderDetail.getId()))
                .merchantId(String.valueOf(orderDetail.getProduct().getMerchant().getId()))
                .merchantName(orderDetail.getProduct().getMerchant().getName())
                .productId(String.valueOf(product.getId()))
                .productName(product.getName())
                .quantity(orderDetail.getQuantity())
                .totalPrice(orderDetail.getTotalPrice())
                .build();
    }

    @Transactional
    public OrderDetailResponse updateOrderDetail(User user, UpdateOrderDetailRequest request) {
        this.validationService.validate(request);

        Order order = this.orderRepository.findById(UUID.fromString(request.getOrderId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists"));

        if (!order.getUser().getId().equals(user.getId())) {
            log.debug("user with id {} attempting to access order with id {}", user.getId(), order.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists");
        }

        if (order.getCompletedAt() != null) {
            log.debug("order with id {} already completed at {}", order.getId(), order.getCompletedAt());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "order already completed");
        }

        OrderDetail orderDetail = this.orderDetailRepository.findByOrderAndId(order, UUID.fromString(request.getOrderDetailId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "detail doesn't exists"));

        Product product = this.productRepository.findById(UUID.fromString(request.getProductId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product doesn't exists"));

        if (!product.getMerchant().isOpen()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product unavailable because merchant is closed");
        }

        orderDetail.setProduct(product);
        orderDetail.setQuantity(request.getQuantity());
        this.orderDetailRepository.save(orderDetail);

        return OrderDetailResponse.builder()
                .orderId(String.valueOf(order.getId()))
                .orderDetailId(String.valueOf(orderDetail.getId()))
                .merchantId(String.valueOf(orderDetail.getProduct().getMerchant().getId()))
                .merchantName(orderDetail.getProduct().getMerchant().getName())
                .productId(String.valueOf(product.getId()))
                .productName(product.getName())
                .quantity(orderDetail.getQuantity())
                .totalPrice(orderDetail.getTotalPrice())
                .build();
    }

    @Transactional
    public void deleteOrderDetail(User user, DeleteOrderDetailRequest request) {
        this.validationService.validate(request);

        Order order = this.orderRepository.findById(UUID.fromString(request.getOrderId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists"));

        if (!user.getId().equals(order.getUser().getId())) {
            log.debug("user with id {} attempting to access order with id {}", user.getId(), order.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists");
        }

        if (order.getCompletedAt() != null) {
            log.debug("order with id {} already completed at {}", order.getId(), order.getCompletedAt());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "order already completed");
        }

        OrderDetail orderDetail = this.orderDetailRepository.findByOrderAndId(order, UUID.fromString(request.getOrderDetailId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "detail doesn't exists"));

        this.orderDetailRepository.delete(orderDetail);
    }

    @Transactional
    public OrderWithDetailsResponse checkout(User user, String orderId) {
        Order order = this.orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists"));

        if (!user.getId().equals(order.getUser().getId())) {
            log.debug("user with id {} attempting to access order with id {}", user.getId(), order.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists");
        }

        if (order.getCompletedAt() != null) {
            log.debug("order with id {} already completed at {}", order.getId(), order.getCompletedAt());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "order already completed");
        }

        order.setCompletedAt(new Date());
        this.orderRepository.save(order);

        return OrderWithDetailsResponse.builder()
                .id(String.valueOf(order.getId()))
                .orderAt(order.getOrderAt())
                .destinationAddress(order.getDestinationAddress())
                .completedAt(order.getCompletedAt())
                .totalPrice(order.getOrderDetails().stream().map(OrderDetail::getTotalPrice)
                        .reduce(0.0, Double::sum))
                .orderDetails(order.getOrderDetails().stream().map(e ->
                        OrderDetailResponse.builder()
                                .orderId(String.valueOf(e.getOrder().getId()))
                                .orderDetailId(String.valueOf(e.getId()))
                                .merchantId(String.valueOf(e.getProduct().getMerchant().getId()))
                                .merchantName(e.getProduct().getMerchant().getName())
                                .productId(String.valueOf(e.getProduct().getId()))
                                .productName(e.getProduct().getName())
                                .quantity(e.getQuantity())
                                .totalPrice(e.getTotalPrice())
                                .build()
                ).toList()).build();
    }
}
