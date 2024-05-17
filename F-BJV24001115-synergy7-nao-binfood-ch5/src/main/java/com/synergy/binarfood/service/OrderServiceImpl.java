package com.synergy.binarfood.service;

import com.synergy.binarfood.entity.Order;
import com.synergy.binarfood.entity.OrderDetail;
import com.synergy.binarfood.entity.Product;
import com.synergy.binarfood.entity.User;
import com.synergy.binarfood.model.order.*;
import com.synergy.binarfood.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ValidationService validationService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;

    @Transactional
    public OrderResponse create(OrderCreateRequest request) {
        this.validationService.validate(request);

        User user = this.userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exists"));
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

    @Transactional
    public OrderDetailResponse createOrderDetail(OrderDetailRequest request) {
        this.validationService.validate(request);

        User user = this.userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exists"));
        Order order = this.orderRepository
                .findById(UUID.fromString(request.getOrderId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists"));
        if (!user.getId().equals(order.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists");
        }

        if (order.getCompletedAt() != null) {
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
    public OrderDetailResponse updateOrderDetail(OrderDetailRequest request) {
        this.validationService.validate(request);

        User user = this.userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exists"));
        Order order = this.orderRepository
                .findById(UUID.fromString(request.getOrderId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists"));
        if (!user.getId().equals(order.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists");
        }

        if (order.getCompletedAt() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "order already completed");
        }

        Product product = this.productRepository.findById(UUID.fromString(request.getProductId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product doesn't exists"));
        if (!product.getMerchant().isOpen()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product unavailable because merchant is closed");
        }

        OrderDetail orderDetail = this.orderDetailRepository.findByOrderAndProduct(order, product)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists"));
        orderDetail.setQuantity(request.getQuantity());
        orderDetail.setTotalPrice(request.getQuantity() * product.getPrice());
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
    public void deleteOrderDetail(OrderDetailDeleteRequest request) {
        this.validationService.validate(request);

        User user = this.userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exists"));
        Order order = this.orderRepository
                .findById(UUID.fromString(request.getOrderId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists"));
        if (!user.getId().equals(order.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists");
        }

        if (order.getCompletedAt() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "order already completed");
        }

        Product product = this.productRepository.findById(UUID.fromString(request.getProductId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product doesn't exists"));
        if (!product.getMerchant().isOpen()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product unavailable because merchant is closed");
        }

        OrderDetail orderDetail = this.orderDetailRepository.findByOrderAndProduct(order, product)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists"));
        this.orderDetailRepository.delete(orderDetail);
    }

    @Transactional
    public InvoiceResponse checkoutOrder(OrderCheckoutRequest request) {
        this.validationService.validate(request);

        User user = this.userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exists"));
        Order order = this.orderRepository
                .findById(UUID.fromString(request.getOrderId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists"));
        if (!user.getId().equals(order.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists");
        }

        if (order.getCompletedAt() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "order already completed");
        }
        order.setCompletedAt(new Date());
        this.orderRepository.save(order);

        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        DecimalFormat decFormat = new DecimalFormat("0.#");

        List<InvoiceItemResponse> invoiceItem = order.getOrderDetails()
                .stream()
                .map(oD -> InvoiceItemResponse.builder()
                        .merchantName(oD.getProduct().getMerchant().getName())
                        .productName(oD.getProduct().getName())
                        .productQuantity(String.format("%d pcs", oD.getQuantity()))
                        .productPrice(String.format("Rp. %s", decFormat.format(oD.getProduct().getPrice())))
                        .productTotalPrice(String.format("Rp. %s", decFormat.format(oD.getTotalPrice())))
                        .build())
                .toList();
        InvoiceResponse invoice = InvoiceResponse.builder()
                .username(user.getUsername())
                .address(order.getDestinationAddress())
                .totalPrice(String.format("Rp. %s", decFormat.format(order
                        .getOrderDetails()
                        .stream()
                        .map(OrderDetail::getTotalPrice)
                        .reduce(0.0, Double::sum))))
                .completedAt(dateFormat.format(order.getCompletedAt()))
                .items(invoiceItem)
                .build();
        System.out.println(invoice.getCompletedAt());
        System.out.println(invoice.getTotalPrice());
        return invoice;
    }

    @Transactional
    public Page<OrderResponse> findAllByUser(OrderGetRequest request) {
        User user = this.userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exists"));

        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize());
        Page<Order> orders = this.orderRepository.findAllByUser(user, pageable);

        return orders.map(new Function<Order, OrderResponse>() {
            @Override
            public OrderResponse apply(Order order) {
                return OrderResponse.builder()
                        .id(String.valueOf(order.getId()))
                        .orderAt(order.getOrderAt())
                        .totalPrice(order.getOrderDetails()
                                .stream()
                                .map(OrderDetail::getTotalPrice)
                                .reduce(0.0, Double::sum))
                        .destinationAddress(order.getDestinationAddress())
                        .completedAt(order.getCompletedAt())
                        .build();
            }
        });
    }

    @Transactional
    public Page<OrderDetailResponse> findOrderDetailsByOrder(OrderDetailGetRequest request) {
        this.validationService.validate(request);

        User user = this.userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exists"));
        Order order = this.orderRepository
                .findById(UUID.fromString(request.getOrderId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists"));
        if (!user.getId().equals(order.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exists");
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize());
        Page<OrderDetail> orderDetails = this.orderDetailRepository.findAllByOrder(order, pageable);

        return  orderDetails.map(new Function<OrderDetail, OrderDetailResponse>() {
            @Override
            public OrderDetailResponse apply(OrderDetail orderDetail) {
                return OrderDetailResponse.builder()
                        .orderId(orderDetail.getOrder().getId().toString())
                        .orderDetailId(orderDetail.getId().toString())
                        .merchantId(orderDetail.getProduct().getMerchant().getId().toString())
                        .merchantName(orderDetail.getProduct().getMerchant().getName())
                        .productId(orderDetail.getProduct().getId().toString())
                        .productName(orderDetail.getProduct().getName())
                        .quantity(orderDetail.getQuantity())
                        .totalPrice(orderDetail.getTotalPrice())
                        .build();
            }
        });
    }
}
