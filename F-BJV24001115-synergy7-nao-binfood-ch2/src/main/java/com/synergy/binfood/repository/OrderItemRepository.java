package com.synergy.binfood.repository;

import com.synergy.binfood.entity.OrderItem;

public class OrderItemRepository {
    public OrderItemRepository() {
        super();
    }

    public boolean isExists(String orderId, String menuCode, String variantCode) {
        if (variantCode != null) {
            return Repository.orderItems.containsKey(String.format(Repository.composite3KeyFormat, orderId, menuCode, variantCode));
        } else {
            return Repository.orderItems.containsKey(String.format(Repository.composite2KeyFormat, orderId, menuCode));
        }
    }

    public OrderItem find(String orderId, String menuCode, String variantCode) {
        if (variantCode != null) {
            return Repository.orderItems.get(String.format(Repository.composite3KeyFormat, orderId, menuCode, variantCode));
        } else {
            return Repository.orderItems.get(String.format(Repository.composite2KeyFormat, orderId, menuCode));
        }
    }

    public void create(OrderItem orderItem) {
        if (orderItem.getVariantCode() != null) {
            Repository.orderItems.put(String.format(Repository.composite3KeyFormat, orderItem.getOrderId(),
                    orderItem.getMenuCode(), orderItem.getVariantCode()), orderItem);
        } else {
            Repository.orderItems.put(String.format(Repository.composite2KeyFormat, orderItem.getOrderId(),
                    orderItem.getMenuCode()), orderItem);
        }
    }

    public void update(OrderItem orderItem) {
        if (orderItem.getVariantCode() != null) {
            Repository.orderItems.put(String.format(Repository.composite3KeyFormat, orderItem.getOrderId(),
                    orderItem.getMenuCode(), orderItem.getVariantCode()), orderItem);
        } else {
            Repository.orderItems.put(String.format(Repository.composite2KeyFormat, orderItem.getOrderId(),
                    orderItem.getMenuCode()), orderItem);
        }
    }

    public void delete(OrderItem orderItem) {
        if (orderItem.getVariantCode() != null) {
            Repository.orderItems.remove(String.format(Repository.composite3KeyFormat, orderItem.getOrderId(),
                    orderItem.getMenuCode(), orderItem.getVariantCode()));
        } else {
            Repository.orderItems.remove(String.format(Repository.composite2KeyFormat, orderItem.getOrderId(),
                    orderItem.getMenuCode()));
        }
    }
}
