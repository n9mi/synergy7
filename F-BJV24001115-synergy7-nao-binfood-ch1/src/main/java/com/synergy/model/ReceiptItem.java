package com.synergy.model;

public class ReceiptItem {
    public final String menuName;
    public final int menuQuantity;
    public final int menuPrice;

    public ReceiptItem(String menuName, int menuQuantity, int menuPrice) {
        this.menuName = menuName;
        this.menuQuantity = menuQuantity;
        this.menuPrice = menuPrice;
    }
}
