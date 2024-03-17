package com.synergy.service;

import com.synergy.model.Menu;
import com.synergy.model.Transaction;
import com.synergy.repository.MenuRepository;
import com.synergy.repository.TransactionRepository;
import dnl.utils.text.table.TextTable;

public class RestaurantService {
    private final MenuRepository menuRepository;
    private final TransactionRepository transactionRepository;
    private TextTable menusTextTable;
    private TextTable chosenMenuTextTable;
    private TextTable currTransactionTextTable;

    public RestaurantService(MenuRepository menuRepository, TransactionRepository transactionRepository) {
        this.menuRepository = menuRepository;
        this.transactionRepository = transactionRepository;

        this.loadMenuTextTable();
    }

    public boolean isMenuExists(String menuId) {
        return this.menuRepository.isExists(menuId);
    }

    public TextTable getMenusTextTable() {
        return menusTextTable;
    }

    public TextTable getChosenMenuTextTable() {
        return chosenMenuTextTable;
    }

    public TextTable getCurrTransactionTextTable() {
        return currTransactionTextTable;
    }

    private void loadMenuTextTable() {
        String[] columnNames = {
                "CODE",
                "MENU",
                "PRICE"
        };

        String[][] menusData = new String[this.menuRepository.count() + 2][3];
        int row = 0;
        for (Menu menu : this.menuRepository.findAll()) {
            menusData[row][0] = menu.id;
            menusData[row][1] = menu.name;
            menusData[row][2] = String.format("Rp. %,d", menu.price);
            row++;
        }

        menusData[row][0] = "O";
        menusData[row][1] = "Order and pay";
        menusData[row][2] = "";

        menusData[row + 1][0] = "X";
        menusData[row + 1][1] = "Stop app";
        menusData[row + 1][2] = "";

        this.menusTextTable = new TextTable(columnNames, menusData);
    }

    public void loadChosenMenuTextTable(String menuId) {
        String[] columnNames = {
                "CODE",
                "MENU",
                "PRICE",
                "ORDERED"
        };

        Menu menu = this.menuRepository.findById(menuId);
        String[][] menuData = new String[5][4];
        menuData[0][0] = menu.id;
        menuData[0][1] = menu.name;
        menuData[0][2] = String.format("Rp. %,d", menu.price);
        // if menu already ordered
        boolean isOrdered = this.transactionRepository.isExists(menu.id);
        if (isOrdered) {
            menuData[0][3] = String.valueOf(this.transactionRepository.findByMenuId(menu.id).quantity);
        } else {
            menuData[0][3] = String.valueOf(0);
        }

        menuData[1][0] = "0";
        menuData[1][1] = "Press 0 to back to menu";

        if (!isOrdered) {
            menuData[2][0] = "1";
            menuData[2][1] = "Press 1 to add quantity";
        }

        if (isOrdered) {
            menuData[3][0] = "2";
            menuData[3][1] = "Press 2 to edit quantity";
            menuData[4][0] = "3";
            menuData[4][1] = "Press 3 delete order";
        }

        this.chosenMenuTextTable = new TextTable(columnNames, menuData);
    }

    public void loadCurrTransactionTextTable() {
        String[] columnNames = {
                "CODE",
                "MENU",
                "PRICE",
                "QTY"
        };

        String[][] transactionData = new String[this.transactionRepository.count() + 2][4];
        int row = 0;
        for (Transaction transaction : this.transactionRepository.getAll()) {
            Menu menu = this.menuRepository.findById(transaction.menuId);
            transactionData[row][0] = menu.id;
            transactionData[row][1] = menu.name;
            transactionData[row][2] = String.format("Rp. %,d", menu.price);
            transactionData[row][3] = String.valueOf(transaction.quantity);
            row += 1;
        }
        transactionData[row][0] = "Y";
        transactionData[row][1] = "Confirm";
        transactionData[row + 1][0] = "N";
        transactionData[row + 1][1] = "Back to menu";

        this.currTransactionTextTable = new TextTable(columnNames, transactionData);
    }
}
