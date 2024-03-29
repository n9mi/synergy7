package com.synergy.binfood.view;

import com.synergy.binfood.controller.RestaurantController;
import com.synergy.binfood.model.order.CheckIsOrderItemExistsResponse;
import com.synergy.binfood.model.order.OrderIdResponse;
import com.synergy.binfood.repository.Repository;

import java.util.InputMismatchException;
import java.util.Scanner;

public class RestaurantView {
    private final RestaurantController restaurantController;

    // this represents current order
    private String currentOrderId;
    private String currentMenuCode;
    private int currQuantity;
    private String currentVariantCode;

    public RestaurantView(RestaurantController restaurantController) {
        this.restaurantController = restaurantController;
    }

    public void askMenu() {
        System.out.print("Choose menu => ");
        Scanner sc = new Scanner(System.in);
        String option = sc.nextLine();

        if (option.equals("X")) {
            this.currentMenuCode = "X";
            return;
        }

        this.currentMenuCode = option;
    }

    public void askMenuVariant() {
        while(true) {
            try {
                this.restaurantController.getMenuWithVariants(this.currentMenuCode).printTable();
                System.out.print("Choose variant => ");
                Scanner sc = new Scanner(System.in);
                String option = sc.nextLine();

                if (option.equals("X")) {
                    this.currentVariantCode = "X";
                    return;
                }

                if (this.restaurantController.checkMenuHasVariant(this.currentMenuCode, option)) {
                    this.currentVariantCode = option;
                    return;
                }

                throw new InputMismatchException("Variant is not exists");
            } catch (Exception e) {
                System.out.println("Input error : " + e.getMessage());
            }
        }
    }

    private void askQuantityInput() {
        while(true) {
            try {
                System.out.print("Insert quantity => ");
                Scanner sc = new Scanner(System.in);
                int qty = sc.nextInt();

                if (qty < 1) {
                    throw new InputMismatchException("quantity can't be zero");
                }

                this.currQuantity = qty;
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid quantity input : " + e.getMessage());
            }
        }
    }

    private String getUpdateDeleteOption() {
        String option;
        while(true) {
            System.out.print("Option => ");
            Scanner sc = new Scanner(System.in);
            option = sc.nextLine();

            if (option.equals("E") || option.equals("D")) {
                return option;
            } else {
                System.out.println("Please type E for Edit or D for Delete");
            }
        }
    }

    public void run() {
        // Initialize order
        OrderIdResponse orderIdResponse = this.restaurantController.createOrder();
        this.currentOrderId = orderIdResponse.getOrderId();

        label:
        while (true) {
            try {
                // Input main menu
                this.restaurantController.getMenus().printTable();
                this.askMenu();
                switch (this.currentMenuCode) {
                    case "X":
                        break label;
                    case "O":
                        this.restaurantController.getOrderedItems(this.currentOrderId).printTable();
                        break;
                    case "P":
                        this.restaurantController.payOrder(this.currentOrderId);
                        break label;
                    default:
                        // If menu has variant, has for its variant
                        if (this.restaurantController.checkMenuHasVariants(this.currentMenuCode)) {
                            this.askMenuVariant();
                        }
                        CheckIsOrderItemExistsResponse checkIsOrderItemExistsResponse = this.restaurantController.
                                checkIsOrderItemExists(this.currentOrderId, this.currentMenuCode, this.currentVariantCode);
                        if (checkIsOrderItemExistsResponse.isOrderItemExists()) {
                            // If order item already exists, display update or delete
                            this.restaurantController.getOrderedItem(this.currentOrderId, this.currentMenuCode,
                                    this.currentVariantCode).printTable();
                            String updateDeleteOption = this.getUpdateDeleteOption();
                            if (updateDeleteOption.equals("E")) {
                                // To edit, ask quantity to renew curQuantity
                                this.askQuantityInput();
                                this.restaurantController.updateOrderItem(this.currentOrderId, this.currentMenuCode,
                                        this.currentVariantCode, this.currQuantity);
                            } else if (updateDeleteOption.equals("D")) {
                                // Delete quantity
                                this.restaurantController.deleteOrderItem(this.currentOrderId, this.currentMenuCode,
                                        this.currentVariantCode);
                            }
                        } else {
                            // If order item not exists yet, only ask for quantity
                            this.askQuantityInput();
                            this.restaurantController.createOrderItem(this.currentOrderId, this.currentMenuCode,
                                    this.currentVariantCode, this.currQuantity);
                        }
                        break;
                }
            } catch (Exception e) {
                System.out.println("ERROR!");
            }
        }

        this.currentOrderId = null;
        this.currentMenuCode = null;
        this.currentVariantCode = null;
        this.currQuantity = 0;
    }
}
