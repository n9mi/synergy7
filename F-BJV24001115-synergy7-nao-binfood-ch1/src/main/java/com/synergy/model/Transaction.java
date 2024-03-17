package com.synergy.model;

public class Transaction {
    public final String menuId;
    public int quantity;

    public Transaction(String menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }
}
