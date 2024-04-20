package com.synergy.binfood.repository;

import com.synergy.binfood.entity.OrderDetail;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class OrderDetailRepository {
    public List<OrderDetail> findAllByOrderId(int orderId) {
        return Repository.orderDetails.values().stream()
                .filter(orderDetail -> orderDetail.getOrderId() == orderId)
                .collect(Collectors.toList());
    }

    public boolean isExistsByOrderAndProductId(int orderId, int productId) {
        return Repository.orderDetails.values().stream().
                anyMatch(orderDetail -> (orderDetail.getOrderId() == orderId) &&
                        orderDetail.getProductId() == productId);
    }

    public OrderDetail findByOrderAndProductId(int orderId, int productId) {
        return Repository.orderDetails.values().stream()
                .filter(orderDetail -> (orderDetail.getOrderId() == orderId) &&
                        orderDetail.getProductId() == productId)
                .findFirst().orElse(null); // OPTIONAL
    }

    public OrderDetail create(OrderDetail orderDetail) {
        int currOrderDetailSize = Repository.orderDetails.size();
        int newOrderDetailIndex = currOrderDetailSize + 1;

        orderDetail.setId(newOrderDetailIndex);
        Repository.orderDetails.put(orderDetail.getId(), orderDetail);

        return orderDetail;
    }

    public void update(OrderDetail orderDetail) {
        Repository.orderDetails.put(orderDetail.getId(), orderDetail);
    }

    public void delete(int orderDetailId) {
        Repository.orderDetails.remove(orderDetailId);
    }
}
