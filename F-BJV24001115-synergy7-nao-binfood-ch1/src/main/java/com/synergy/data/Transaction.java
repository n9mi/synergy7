package com.synergy.data;

import java.util.*;

public class Transaction {
    private final String id;

    // Map represents <MenuID, quantity>
    private Map<Integer , Integer> items;
    private int totalPrice;

    public Transaction() {
        this.id = UUID.randomUUID().toString();
        this.items = new HashMap<>();
        this.totalPrice = 0;
    }

    public boolean addItems(Menu menu, int n) {
        if (n < 1) {
            return  false;
        }

        if (this.items.containsKey(menu.getId())) {
            this.totalPrice = Math.min(this.totalPrice - (menu.getPrice() * this.items.get(menu.getId())), 0);
        }

        this.items.put(menu.getId(), n);
        this.totalPrice += menu.getPrice() * n;

        return  true;
    }

    public String getId() {
        return id;
    }

    public Map<Integer, Integer> getItems() {
        return Collections.unmodifiableMap(this.items);
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
