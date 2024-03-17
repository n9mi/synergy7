package com.synergy.repository;

import com.synergy.model.Menu;
import com.synergy.model.Transaction;

import java.util.HashMap;
import java.util.Map;

public class TransactionRepository {
    private final Map<String, Transaction> transactions;
    private int totalPrice;

    public TransactionRepository() {
        this.transactions = new HashMap<>();
        this.totalPrice = 0;
    }

    public Transaction[] getAll() {
        return this.transactions.values().toArray(new Transaction[0]);
    }

    public int count() {
        return this.transactions.size();
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public boolean isExists(String menuId) {
        return this.transactions.containsKey(menuId);
    }

    public Transaction findByMenuId(String menuId) {
        return this.transactions.get(menuId);
    }

    public void insert(Menu menu, int quantity) {
        this.transactions.put(menu.id, new Transaction(menu.id, quantity));
        this.totalPrice += menu.price * quantity;
    }

    public void update(Menu menu, int quantity)  {
        Transaction transaction = this.findByMenuId(menu.id);
        this.totalPrice = Math.min(this.totalPrice - (menu.price * transaction.quantity), 0);

        transaction.quantity = quantity;
        this.transactions.put(menu.id, transaction);
        this.totalPrice += menu.price * quantity;
    }

    public void delete(Menu menu) {
        this.totalPrice = Math.min(this.totalPrice - (menu.price * this.transactions.get(menu.id).quantity), 0);
        this.transactions.remove(menu.id);
    }
}
