package com.synergy.binfood.repository;

import com.synergy.binfood.entity.Order;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class OrderRepository {
    public Order findById(int id) {
        return Repository.orders.get(id);
    }

    public boolean isOwnByUser(int orderId, int userId) {
        return (Repository.orders.containsKey(orderId)) &&
                (Repository.orders.get(orderId).getUserId() == userId);
    }

    public Order create(Order order) {
        int currOrderSize = Repository.orders.size();
        int newOrderIndex = currOrderSize + 1;

        order.setId(newOrderIndex);
        order.setCompleted(false);
        order.setCreatedAt(new Date());
        Repository.orders.put(newOrderIndex, order);

        return order;
    }
}
