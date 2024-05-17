package com.synergy.binarfood.controller.buyer;

import com.synergy.binarfood.model.order.*;
import com.synergy.binarfood.model.web.WebResponse;
import com.synergy.binarfood.service.JasperService;
import com.synergy.binarfood.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buyer/orders")
@RequiredArgsConstructor
public class BuyerOrderController {
    private final OrderService orderService;
    private final JasperService jasperService;

    @PostMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<OrderResponse> create(
            Authentication authentication,
            @RequestBody OrderCreateRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        request.setUsername(userDetails.getUsername());

        OrderResponse response = this.orderService.create(request);
        return WebResponse.<OrderResponse>builder().data(response).build();
    }

    @PostMapping(
            path = "/{orderId}/order-details",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<OrderDetailResponse> createOrderDetail(
            Authentication authentication,
            @PathVariable("orderId") String orderId,
            @RequestBody OrderDetailRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        request.setOrderId(orderId);
        request.setUsername(userDetails.getUsername());

        OrderDetailResponse response = this.orderService.createOrderDetail(request);
        return WebResponse.<OrderDetailResponse>builder().data(response).build();
    }

    @PutMapping(
            path = "/{orderId}/order-details",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<OrderDetailResponse> updateOrderDetail(
            Authentication authentication,
            @PathVariable("orderId") String orderId,
            @RequestBody OrderDetailRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        request.setOrderId(orderId);
        request.setUsername(userDetails.getUsername());

        OrderDetailResponse response = this.orderService.updateOrderDetail(request);
        return WebResponse.<OrderDetailResponse>builder().data(response).build();
    }

    @DeleteMapping(
            path = "/{orderId}/order-details",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteOrderDetail(
            Authentication authentication,
            @PathVariable("orderId") String orderId,
            @RequestBody OrderDetailDeleteRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        request.setOrderId(orderId);
        request.setUsername(userDetails.getUsername());

        this.orderService.deleteOrderDetail(request);
        return WebResponse.<String>builder().data(null).build();
    }

    @PostMapping(
            path = "/{orderId}/checkout",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Resource> checkoutOrder(
            Authentication authentication,
            @PathVariable("orderId") String orderId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        OrderCheckoutRequest request = OrderCheckoutRequest.builder()
                        .orderId(orderId)
                        .username(userDetails.getUsername())
                    .build();

        InvoiceResponse response = this.orderService.checkoutOrder(request);
        byte[] reportContent = jasperService.getInvoiceReport(response);
        ByteArrayResource resource = new ByteArrayResource(reportContent);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(String
                                        .format("invoice_%s.pdf",
                                                response.getCompletedAt()))
                                .build()
                                .toString())
                .body(resource);
    }

    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<OrderResponse>> getAllByCurrentUser(
            Authentication authentication,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        OrderGetRequest request = OrderGetRequest.builder()
                .page(page)
                .pageSize(pageSize)
                .username(userDetails.getUsername())
                .build();
        Page<OrderResponse> response = this.orderService.findAllByUser(request);

        return WebResponse.<List<OrderResponse>>builder()
                .data(response.getContent())
                .build();
    }

    @GetMapping(
            path = "/{orderId}/order-details",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<OrderDetailResponse>> getOrderDetailsByCurrentUser(
            Authentication authentication,
            @PathVariable("orderId") String orderId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        OrderDetailGetRequest request = OrderDetailGetRequest.builder()
                .page(page)
                .pageSize(pageSize)
                .username(userDetails.getUsername())
                .orderId(orderId)
                .build();
        Page<OrderDetailResponse> response = this.orderService.findOrderDetailsByOrder(request);

        return WebResponse.<List<OrderDetailResponse>>builder()
                .data(response.getContent())
                .build();
    }
}
