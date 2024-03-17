package com.synergy.model;

public class Receipt {
    public final String id;

    public final String merchantName;
    public final ReceiptItem[] items;
    public final int totalPrice;

    public Receipt(String id, String merchantName, ReceiptItem[] items, int totalPrice) {
        this.id = id;
        this.merchantName = merchantName;
        this.items = items;
        this.totalPrice = totalPrice;
    }
}
