package com.synergy.service;

import com.synergy.model.Menu;
import com.synergy.model.Receipt;
import com.synergy.model.ReceiptItem;
import com.synergy.model.Transaction;
import com.synergy.repository.MenuRepository;
import com.synergy.repository.TransactionRepository;
import com.synergy.utils.ReceiptWriter;

import java.util.Map;
import java.util.UUID;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final MenuRepository menuRepository;

    public TransactionService(TransactionRepository transactionRepository, MenuRepository menuRepository) {
        this.transactionRepository = transactionRepository;
        this.menuRepository = menuRepository;
    }

    public boolean isExists(String menuId) {
        return this.transactionRepository.isExists(menuId);
    }

    public void add(String menuId, int quantity) throws RuntimeException {
        if (quantity < 1) {
            throw new RuntimeException("transaction error : invalid quantity");
        }

        if (!this.menuRepository.isExists(menuId)) {
            throw new RuntimeException(String.format("transaction error : can't find menu with id %s", menuId));
        }

        if (this.transactionRepository.isExists(menuId)) {
            throw new RuntimeException(
                    String.format("transaction error : transaction with menu id %s already exists, pleas edit instead",
                            menuId));
        }

        Menu menu = this.menuRepository.findById(menuId);
        this.transactionRepository.insert(menu, quantity);
    }

    public void update(String menuId, int quantity) throws RuntimeException {
        if (quantity < 1) {
            throw new RuntimeException("transaction error : invalid quantity");
        }

        if (!this.menuRepository.isExists(menuId)) {
            throw new RuntimeException(String.format("transaction error : can't find menu with id %s", menuId));
        }

        if (!this.transactionRepository.isExists(menuId)) {
            throw new RuntimeException(String.format("transaction error : can't find transaction with menu id %s", menuId));
        }

        Menu menu = this.menuRepository.findById(menuId);
        this.transactionRepository.update(menu, quantity);
    }

    public void delete(String menuId) throws RuntimeException {
        if (!this.menuRepository.isExists(menuId)) {
            throw new RuntimeException(String.format("transaction error : can't find menu with id %s", menuId));
        }

        if (!this.transactionRepository.isExists(menuId)) {
            throw new RuntimeException(String.format("transaction error : can't find transaction with menu id %s", menuId));
        }

        Menu menu = this.menuRepository.findById(menuId);
        this.transactionRepository.delete(menu);
    }

    public void generateReceipt(String merchantName) throws RuntimeException {
        if (this.transactionRepository.count() < 1) {
            throw new RuntimeException("transaction amount can't be zero");
        }

        ReceiptItem[] receiptItems = new ReceiptItem[this.transactionRepository.count()];
        int counter = 0;
        for (Transaction transaction : this.transactionRepository.getAll()) {
            Menu menu = this.menuRepository.findById(transaction.menuId);
            receiptItems[counter] = new ReceiptItem(menu.name, transaction.quantity, menu.price);
            counter++;
        }

        Receipt receipt = new Receipt(UUID.randomUUID().toString(), merchantName, receiptItems, this.transactionRepository.getTotalPrice());
        ReceiptWriter.writeToPDF("receipts", receipt);
    }
}
