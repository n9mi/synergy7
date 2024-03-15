package com.synergy.data;

public class ReceiptItem {
    private String itemName;
    private int itemPrice;
    private int itemQuantity;

    public ReceiptItem(String itemName, int itemPrice, int itemQuantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
    }

    public String getString() {
        return String.format("%s: Rp. %d x %d pcs ", this.itemName, this.itemPrice, this.itemQuantity);
    }
}
