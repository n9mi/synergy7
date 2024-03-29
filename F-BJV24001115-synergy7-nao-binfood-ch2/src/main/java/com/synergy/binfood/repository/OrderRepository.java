package com.synergy.binfood.repository;

import com.synergy.binfood.entity.Order;
import com.synergy.binfood.entity.OrderItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OrderRepository {
    public OrderRepository() {
        super();
    }

    public boolean isExists(String id) {
        return Repository.orders.containsKey(id);
    }

    public List<Order> findAll() {
        return new ArrayList<>(Repository.orders.values());
    }

    public Order find(String id) {
        return Repository.orders.get(id);
    }

    public Order create(Order order) {
        order.setId(UUID.randomUUID().toString());
        Repository.orders.put(order.getId(), order);

        return order;
    }

    public void pay(Order order) {
        order.setPayAt(new Date());
        Repository.orders.put(order.getId(), order);
    }

    public boolean isOrderItemExistsOnOrder(String orderId, String menuCode, String variantCode) {
        if (variantCode != null) {
            return Repository.orderItems.containsKey(String.format(Repository.composite3KeyFormat,
                    orderId, menuCode, variantCode));
        } else {
            return Repository.orderItems.containsKey(String.format(Repository.composite2KeyFormat,
                    orderId, menuCode));
        }
    }

    public List<OrderItem> findOrderItemsFromOrder(Order order) {
        List<OrderItem> orderItemsResult = new ArrayList<>();
        Repository.orderItems.forEach((K, V) -> {
            if (K.startsWith(order.getId())) {
                orderItemsResult.add(V);
            }
        });
        return orderItemsResult;
    }
}
