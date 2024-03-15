package com.synergy.service;

import com.synergy.data.Menu;
import com.synergy.data.ReceiptItem;
import com.synergy.data.Transaction;
import com.synergy.utils.MenuReader;
import com.synergy.utils.ReceiptWriter;
import dnl.utils.text.table.TextTable;

import javax.sound.midi.Soundbank;
import java.util.*;

public class RestaurantService {
    private final String restaurantName;
    private List<Menu> menus;
    private TextTable menusTextTable;
    private TextTable transactionTextTable;
    private Transaction transaction;

    public RestaurantService(String restaurantName) {
        this.restaurantName = restaurantName;
        this.transaction = new Transaction();
    }

    private void loadMenu() throws RuntimeException {
        MenuReader menuReader = new MenuReader("menus.csv");
        try {
            menuReader.readMenuFromCSV();
        } catch (RuntimeException e) {
            throw e;
        }
        this.menus = menuReader.getMenus();
    }

    private void generateReceipt() throws RuntimeException {
        List<ReceiptItem> receiptItems = new ArrayList<>();

        Map<Integer, Integer> transactionItems = this.transaction.getItems();
        for (Map.Entry<Integer, Integer> item : transactionItems.entrySet()) {
            Menu menu = this.menus.get(item.getKey());
            receiptItems.add(new ReceiptItem(menu.getName(), menu.getPrice(), item.getKey()));
        }

        ReceiptWriter receiptWriter = new ReceiptWriter(this.restaurantName, receiptItems, this.transaction.getTotalPrice());
        receiptWriter.generateReceipt();
    }

    private void loadMenuTextTable() {
        String[] columnNames = {
                "Opsi",
                "Nama",
                "Harga"
        };
        int menusCount = this.menus.toArray().length;
        String[][] menusData = new String[menusCount + 2][3];

        for (int i=0; i<menusCount; i++) {
            menusData[i][0] = String.valueOf(this.menus.get(i).getId() + 1);
            menusData[i][1] = this.menus.get(i).getName();
            menusData[i][2] = String.format("Rp. %d", this.menus.get(i).getPrice());
        }
        menusData[menusCount][0] = String.valueOf(menusCount + 1);
        menusData[menusCount][1] = "Pesan dan bayar";
        menusData[menusCount][2] = "";

        menusData[menusCount + 1][0] = String.valueOf(0);
        menusData[menusCount + 1][1] = "Keluar aplikasi";
        menusData[menusCount + 1][2] = "";

        this.menusTextTable = new TextTable(columnNames, menusData);
    }

    private void loadTransactionTextTable() {
        String[] columnNames = {
                "Menu",
                "Harga",
                "Quantity"
        };

        Map<Integer, Integer> transactionItems = this.transaction.getItems();
        int itemsCount = transactionItems.size();
        String[][] itemsData = new String[itemsCount][3];

        int row = 0;
        for (Map.Entry<Integer, Integer> item : transactionItems.entrySet()) {
            Menu menu = this.menus.get(item.getKey());
            itemsData[row][0] = menu.getName();
            itemsData[row][1] = String.format("Rp. %d", menu.getPrice());
            itemsData[row][2] = String.valueOf(item.getValue());
            row += 1;
        }

        this.transactionTextTable = new TextTable(columnNames, itemsData);
    }

    private int chooseOption() {
        int option = 0;
        boolean continueWaiting = true;

        while(continueWaiting) {
            try {
                System.out.print("-> ");
                Scanner sc = new Scanner(System.in);
                option = sc.nextInt();

                if (option < 0) {
                    throw new InputMismatchException();
                }

                continueWaiting = false;
            } catch (InputMismatchException e) {
                System.out.println("Silahkan masukan pilihan Anda!");
            }
        }
        System.out.println();

        return option;
    }

    public void displayMenu(int id) {
        Menu menu = this.menus.get(id);
        System.out.format("\n%s\t|%15s%n", menu.getName(), "Rp. " + menu.getPrice());
        System.out.println("Tekan 0 untuk kembali ke menu...");
    }

    public void run() throws RuntimeException {
        this.loadMenu();
        this.loadMenuTextTable();

        int menusCount = this.menus.toArray().length;
        boolean chooseMenuSession = true;
        int option = 0;

        while (chooseMenuSession) {
            System.out.println("Welcome to " + this.restaurantName + "!");
            this.menusTextTable.printTable();

            System.out.println("Masukan opsi Anda : ");
            option = this.chooseOption();
            if (option == 0) {
                chooseMenuSession = false;
            } else if (option > 0 && option <= menusCount) {
                int menuId = option - 1;
                this.displayMenu(menuId);
                System.out.print("quantity");
                option = this.chooseOption();
                if (option > 0) {
                    Menu menu = this.menus.get(menuId);
                    this.transaction.addItems(this.menus.get(menuId), option);
                }
            } else if (option == (menusCount + 1)) {
                System.out.println("\nKonfirmasi pembayaran");
                this.loadTransactionTextTable();
                this.transactionTextTable.printTable();
                System.out.printf("Total bayar : Rp. %d\n%n", this.transaction.getTotalPrice());
                System.out.println("1. Konfirmasi bayar\n2. Kembali ke menu utama\n0. Keluar aplikasi");

                option = this.chooseOption();
                if (option == 1) {
                    this.generateReceipt();
                    chooseMenuSession = false;
                } else if (option == 2) {
                    continue;
                } else {
                    chooseMenuSession = false;
                }
            }else {
                System.out.println("Silahkan pilih opsi!\n");
            }
        }

        if (option == 0) {
            return;
        }
    }
}
