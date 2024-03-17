package com.synergy.controller;

import com.synergy.model.Menu;
import com.synergy.service.RestaurantService;
import com.synergy.service.TransactionService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class RestaurantController {
    private final String restaurantName;
    private final RestaurantService restaurantService;
    private final TransactionService transactionService;

    public RestaurantController(String restaurantName, RestaurantService restaurantService, TransactionService transactionService) {
        this.restaurantName = restaurantName;
        this.restaurantService = restaurantService;
        this.transactionService = transactionService;
    }

    private void displayMenuScreen() {
        System.out.printf("Welcome to %s restaurant!%n", this.restaurantName);
        this.restaurantService.getMenusTextTable().printTable();
    }

    private void displayCurrTransactionTextTable() {
        System.out.println("Your transaction includes :");
        this.restaurantService.loadCurrTransactionTextTable();
        this.restaurantService.getCurrTransactionTextTable().printTable();
    }

    private void displayChosenMenu(String menuId) {
        this.restaurantService.loadChosenMenuTextTable(menuId);
        this.restaurantService.getChosenMenuTextTable().printTable();
    }

    private String getOrderOption() {
        String option;

        while(true) {
            System.out.print("Choose menu -> ");
            Scanner sc = new Scanner(System.in);
            option = sc.nextLine();

            if (this.restaurantService.isMenuExists(option) || option.equals("O") || option.equals("X")) {
                return option;
            }

            System.out.println("invalid menu code!");
        }
    }

    private int getChosenMenuInput(String menuId) {
        int input;
        boolean isOrdered = this.transactionService.isExists(menuId);

        while(true) {
            try {
                System.out.print("Insert option -> ");
                Scanner sc = new Scanner(System.in);
                input = sc.nextInt();

                if ((input < 0) || (!isOrdered && (input > 1)) || (input > 3)) {
                    throw new InputMismatchException(String.format("invalid option %d", input));
                }

                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid quantity input : " + e.getMessage());
            }
        }

        return input;
    }

    private int getQuantityInput() {
        int qty;

        while(true) {
            try {
                System.out.print("Insert quantity -> ");
                Scanner sc = new Scanner(System.in);
                qty = sc.nextInt();

                if (qty < 0) {
                    throw new InputMismatchException("quantity can't be zero");
                }

                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid quantity input : " + e.getMessage());
            }
        }

        return qty;
    }

    private String getPaymentConfirmationOption() {
        String option;

        while(true) {
            System.out.print("Confirm payment (Y/N) -> ");
            Scanner sc = new Scanner(System.in);
            option = sc.nextLine();

            if (option.equals("Y") || option.equals("N")) {
                return option;
            }

            System.out.println("invalid option!");
        }
    }

    public void run() {
        boolean isRunRestaurant = true;

        while(isRunRestaurant) {
            try {
                // Display greeting and menu and ask user to input menu code
                this.displayMenuScreen();
                String chosenMenuId = this.getOrderOption();
                switch (chosenMenuId) {
                    // Option -> order and pay
                    case "O":
                        this.displayCurrTransactionTextTable();
                        String confirmPaymentOption = this.getPaymentConfirmationOption();
                        switch (confirmPaymentOption) {
                            case "Y":
                                this.transactionService.generateReceipt(this.restaurantName);
                                isRunRestaurant = false;
                                break;
                            case "N":
                                break;
                        }
                        break;
                    // Option -> Exit app
                    case "X":
                        isRunRestaurant = false;
                        break;
                    default:
                        boolean runMenuEntry = true;
                        while (runMenuEntry) {
                            try {
                                // Display chosen menu to perform action such as add quantity, edit quantity, or delete order
                                this.displayChosenMenu(chosenMenuId);
                                int option  = this.getChosenMenuInput(chosenMenuId);
                                switch (option) {
                                    // Option 0 -> back to menu
                                    case 0:
                                        break;
                                    // Option 1 -> add quantity
                                    case 1:
                                        int quantityInput = this.getQuantityInput();
                                        this.transactionService.add(chosenMenuId, quantityInput);
                                        break;
                                    case 2:
                                        // Option 2 -> edit quantity
                                        int editQuantityInput = this.getQuantityInput();
                                        this.transactionService.update(chosenMenuId, editQuantityInput);
                                        break;
                                    case 3:
                                        // Option 3 -> delete quantity
                                        this.transactionService.delete(chosenMenuId);
                                }
                                runMenuEntry = false;
                            } catch (Exception e) {
                                System.out.println("ERROR : " + e.getMessage());
                            }
                        }
                }
            } catch (Exception e) {
                System.out.println("ERROR : " + e.getMessage());
                System.out.println("Restarting app...");
            }
        }
    }
}
