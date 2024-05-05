package com.synergy.binarfood.controller;

import com.synergy.binarfood.entity.User;
import com.synergy.binarfood.model.DataResponse;
import com.synergy.binarfood.model.order.*;
import com.synergy.binarfood.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping(
            path = "/api/v1/orders",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<OrderResponse> create(User user, @RequestBody CreateOrderRequest request) {
        OrderResponse order = this.orderService.create(user, request);

        return DataResponse.<OrderResponse>builder().data(order).build();
    }

    @GetMapping(
            path = "/api/v1/orders",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<List<OrderResponse>> getByCurrentUser(User user,
                                                              @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        Page<OrderResponse> orders = this.orderService.findAllByUser(user, page, pageSize);

        return DataResponse.<List<OrderResponse>>builder()
                .data(orders.getContent())
                .build();
    }

    @GetMapping(
            path = "/api/v1/orders/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<OrderWithDetailsResponse> getDetailsById(User user, @PathVariable("id") String id) {
        OrderWithDetailsResponse order = this.orderService.findDetailsById(user, id);

        return DataResponse.<OrderWithDetailsResponse>builder()
                .data(order)
                .build();
    }

    @PostMapping(
            path = "/api/v1/orders/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<OrderDetailResponse> createDetail(User user,
                                                          @PathVariable("id") String id,
                                                          @RequestBody CreateOrderDetailRequest request) {
        request.setOrderId(id);
        OrderDetailResponse orderDetail = this.orderService.createOrderDetail(user, request);

        return DataResponse.<OrderDetailResponse>builder()
                .data(orderDetail)
                .build();
    }

    @PutMapping(
            path = "/api/v1/orders/{orderId}/details/{orderDetailId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<OrderDetailResponse> updateDetail(User user,
                                                          @PathVariable("orderId") String orderId,
                                                          @PathVariable("orderDetailId") String orderDetailId,
                                                          @RequestBody UpdateOrderDetailRequest request) {
        request.setOrderId(orderId);
        request.setOrderDetailId(orderDetailId);
        OrderDetailResponse orderDetail = this.orderService.updateOrderDetail(user, request);

        return DataResponse.<OrderDetailResponse>builder()
                .data(orderDetail)
                .build();
    }

    @DeleteMapping(
            path = "/api/v1/orders/{orderId}/details/{orderDetailId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<String> deleteDetail(User user,
                                              @PathVariable("orderId") String orderId,
                                              @PathVariable("orderDetailId") String orderDetailId) {
        DeleteOrderDetailRequest request = new DeleteOrderDetailRequest(orderId, orderDetailId);
        this.orderService.deleteOrderDetail(user, request);

        return DataResponse.<String>builder().data("").build();
    }

    @PostMapping(
            path = "/api/v1/orders/{id}/checkout",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<OrderWithDetailsResponse> checkout(User user, @PathVariable("id") String id) {
        OrderWithDetailsResponse order = this.orderService.checkout(user, id);

        return DataResponse.<OrderWithDetailsResponse>builder()
                .data(order)
                .build();
    }
}
