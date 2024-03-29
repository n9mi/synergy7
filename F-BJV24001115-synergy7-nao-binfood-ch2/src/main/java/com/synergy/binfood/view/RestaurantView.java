package com.synergy.binfood.view;

import com.synergy.binfood.controller.RestaurantController;
import com.synergy.binfood.model.order.CheckIsOrderItemExistsResponse;
import com.synergy.binfood.model.order.OrderIdResponse;
import dnl.utils.text.table.TextTable;

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
        while(true) {
            System.out.print("Choose menu => ");
            Scanner sc = new Scanner(System.in);
            String option = sc.nextLine();

            if (option.equals("X")) {
                this.currentMenuCode = "X";
                return;
            }

            try {
                this.currentMenuCode = option;
                return;
            } catch (Exception e) {
                System.out.print("ERROR -> ");
            }
        }
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
                }
            } catch (Exception e) {
                System.out.print("ERROR -> ");
            }
        }
    }

    private void askQuantityInput() {
        while(true) {
            try {
                System.out.print("Insert quantity => ");
                Scanner sc = new Scanner(System.in);
                int qty = sc.nextInt();

                if (qty < 0) {
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
        while (true) {
            try {
                // Initialize order
                OrderIdResponse orderIdResponse = this.restaurantController.createOrder();
                this.currentOrderId = orderIdResponse.getOrderId();

                // Input main menu
                this.restaurantController.getMenus().printTable();
                this.askMenu();
                if (this.currentMenuCode.equals("X")) {
                    break;
                }

                // If menu
                if (this.restaurantController.checkMenuHasVariants(this.currentMenuCode)) {
                    this.askMenuVariant();

                    CheckIsOrderItemExistsResponse checkIsOrderItemExistsResponse = this.restaurantController.checkIsOrderItemExists(this.currentOrderId,
                            this.currentMenuCode, this.currentVariantCode);
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
                        // If order don't exist, ask quantity and create order
                        this.askQuantityInput();
                        this.restaurantController.createOrderItem(this.currentOrderId, this.currentMenuCode,
                                this.currentVariantCode, this.currQuantity);
                    }
                } else {
                    //
                    this.askQuantityInput();
                }
            } catch (Exception e) {
                System.out.print("ERROR -> ");
            }
        }
    }
}
