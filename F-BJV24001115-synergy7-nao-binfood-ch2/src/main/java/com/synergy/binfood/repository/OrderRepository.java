package com.synergy.binfood.repository;

import com.synergy.binfood.entity.Order;
import com.synergy.binfood.entity.OrderItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OrderRepository extends Repository {
    public OrderRepository() {
        super();
    }

    public boolean isExists(String id) {
        return this.orders.containsKey(id);
    }

    public List<Order> findAll() {
        return new ArrayList<>(this.orders.values());
    }

    public Order find(String id) {
        return this.orders.get(id);
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
        if (variantCode.isBlank()) {
            return Repository.orderItems.containsKey(String.format(Repository.composite3KeyFormat,
                    orderId, menuCode, variantCode));
        } else {
            return Repository.orderItems.containsKey(String.format(Repository.composite2KeyFormat,
                    orderId, menuCode));
        }
    }

    public List<OrderItem> findOrderItemsFromOrder(Order order) {
        List<OrderItem> orderItems = new ArrayList<>();
        Repository.orderItems.forEach((K, V) -> {
            if (K.startsWith(order.getId())) {
                orderItems.add(V);
            }
        });

        return orderItems;
    }
}
