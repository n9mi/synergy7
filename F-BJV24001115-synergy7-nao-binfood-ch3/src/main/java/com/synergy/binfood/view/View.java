package com.synergy.binfood.view;

import com.synergy.binfood.config.Bootstrap;
import com.synergy.binfood.controller.AuthController;
import com.synergy.binfood.controller.MerchantController;
import com.synergy.binfood.controller.OrderController;
import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.merchant.MerchantResponse;
import com.synergy.binfood.model.merchant.MerchantWithProductsResponse;
import com.synergy.binfood.model.order.OrderDetailResponse;
import com.synergy.binfood.model.order.OrderResponse;
import com.synergy.binfood.model.product.ProductResponse;
import dnl.utils.text.table.TextTable;

import java.util.*;
import java.util.stream.Collectors;

public class View {
    private AuthController authController;
    private MerchantController merchantController;
    private OrderController orderController;

    private boolean appRunning = true;
    private AuthData authData;

    public View(Bootstrap bootstrap) {
        this.authController = bootstrap.authController;
        this.merchantController = bootstrap.merchantController;
        this.orderController = bootstrap.orderController;
    }

    private String getOption(String question, Set<String> options) {
        while (true) {
            System.out.printf("%s => ", question);
            Scanner sc = new Scanner(System.in);
            String chosenOpt = sc.nextLine();

            if (!options.isEmpty()) {
                if (options.contains(chosenOpt)) {
                    System.out.println();
                    return chosenOpt;
                } else {
                    System.out.println("Invalid input");
                }
            } else {
                System.out.println();
                return chosenOpt;
            }
        }
    }

    private int getQuantityInput() {
        while(true) {
            try {
                System.out.print("Insert quantity => ");
                Scanner sc = new Scanner(System.in);
                int qty = sc.nextInt();

                if (qty < 1) {
                    throw new InputMismatchException("quantity can't be zero");
                }

                System.out.println();
                return qty;
            } catch (InputMismatchException e) {
                System.out.println("Invalid quantity input : " + e.getMessage());
            }
        }
    }

    private void login() {
        while (true) {
            String usernameInput = this.getOption("username", Collections.emptySet());
            String passwordInput = this.getOption("password", Collections.emptySet());
            try {
                this.authController.login(usernameInput, passwordInput);
                this.authData = this.authController.getUserInfoByUsername(usernameInput);
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private TextTable merchantsTable(List<MerchantResponse> merchants) {
        String[] columnNames = { "CODE", "NAME", "LOCATION", "-" };
        String[][] rows = new String [ merchants.size() + 2][ columnNames.length ];

        int currRow = 0;
        for (MerchantResponse merchant: merchants) {
            rows[currRow][0] = String.valueOf(merchant.getId());
            rows[currRow][1] = merchant.getMerchantName();
            rows[currRow][2] = merchant.getMerchantLocation();
            rows[currRow][3] = merchant.isOpen() ? "OPEN" : "CLOSED";
            currRow++;
        }

        rows[currRow][0] = "E";
        rows[currRow][1] = "Exit";

        rows[currRow + 1][0] = "O";
        rows[currRow + 1][1] = "View order";

        return new TextTable(columnNames, rows);
    }

    private TextTable orderHistoryTable(List<OrderResponse> orders) {
        String[] columnNames = { "ID", "ORDERED AT", "COMPLETED AT", "DESTINATION ADDRESS" };
        String[][] rows = new String [ orders.size() + 1][ columnNames.length ];

        int currRow = 0;
        for (OrderResponse order: orders) {
            rows[currRow][0] = String.valueOf(order.getId());
            rows[currRow][1] = order.getCreatedAt();
            rows[currRow][2] = order.getCompletedAt();
            rows[currRow][3] = order.getDestinationAddress();
            currRow++;
        }

        rows[currRow][0] = "E";
        rows[currRow][1] = "Exit";

        return new TextTable(columnNames, rows);
    }

    private TextTable orderDetailsHistoryTable(List<OrderDetailResponse> orderDetails) {
        String[] columnNames = { "PRODUCT", "QUANTITY", "PRICE", "TOTAL PRICE" };
        String[][] rows = new String [ orderDetails.size() + 1 ][ columnNames.length ];

        int currRow = 0;
        int totalPrice = 0;
        for (OrderDetailResponse orderDetail: orderDetails) {
            rows[currRow][0] = orderDetail.getProductName();
            rows[currRow][1] = String.format("%d pcs", orderDetail.getProductQuantity());
            rows[currRow][2] = String.format("Rp. %,d", orderDetail.getProductPrice());
            rows[currRow][3] = String.format("Rp. %,d", orderDetail.getTotalPrice());
            currRow++;
            totalPrice += orderDetail.getTotalPrice();
        }

        rows[currRow][3] = String.format("Rp. %,d", totalPrice);

        return new TextTable(columnNames, rows);
    }

    private void orderHistoryMenu() {
        while(true) {
            try {
                List<OrderResponse> orders = this.orderController.getUserOrders(this.authData);
                this.orderHistoryTable(orders).printTable();
                System.out.println();
                Set<String> options = orders.stream().map(o -> String.valueOf(o.getId()))
                        .collect(Collectors.toSet());
                options.add("E");

                String chosenOption = this.getOption("option", options);
                if (chosenOption.equals("E")) {
                    break;
                } else {
                    OrderResponse order = this.orderController.getOrderWithOrderDetails(this.authData,
                            Integer.parseInt(chosenOption));
                    if (!order.getOrderDetails().isEmpty()) {
                        this.orderDetailsHistoryTable(order.getOrderDetails()).printTable();
                        System.out.println();
                    } else {
                        System.out.println("---EMPTY ORDER---");
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void merchantsMenu() {
        try {
            List<MerchantResponse> merchants = this.merchantController.getOpenedMerchants();
            Set<String> options = merchants.stream()
                    .map(m -> String.valueOf(m.getId()))
                    .collect(Collectors.toSet());
            options.add("E");
            options.add("O");

            this.merchantsTable(merchants).printTable();
            System.out.println();
            String chosenOption = this.getOption("insert option", options);
            switch (chosenOption) {
                case "E":
                    this.appRunning = false;
                    break;
                case "O":
                    this.orderHistoryMenu();
                    break;
                default:
                    this.authData.setCurrMerchantId(Integer.parseInt(chosenOption));
                    String destinationAddress = this.getOption("enter destination address",
                            Collections.emptySet());
                    this.authData = this.orderController.createOrder(authData, destinationAddress);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private TextTable merchantDetailsTable(MerchantWithProductsResponse merchantDetails) {
        String[] columnNames = { "CODE", "NAME", "PRICE" };
        String[][] rows = new String [ merchantDetails.getMerchantProducts().size() + 2][ columnNames.length ];

        int currRow = 0;
        for (ProductResponse product: merchantDetails.getMerchantProducts()) {
            rows[currRow][0] = String.valueOf(product.getId());
            rows[currRow][1] = product.getProductName();
            rows[currRow][2] = String.format("Rp. %,d", product.getPrice());
            currRow++;
        }

        rows[currRow][0] = "O";
        rows[currRow][1] = "View order";

        rows[currRow + 1][0] = "C";
        rows[currRow + 1][1] = "Check out";

        return new TextTable(columnNames, rows);
    }

    private TextTable productDetailTable(ProductResponse product) {
        String[] columnNames = { "CODE", "NAME", "PRICE" };
        String[][] rows = new String [1][3];

        rows[0][0] = String.valueOf(product.getId());
        rows[0][1] = product.getProductName();
        rows[0][2] = String.format("Rp. %,d", product.getPrice());

        return new TextTable(columnNames, rows);
    }

    private TextTable orderOptionTable() {
        String[] columnNames = { "CODE", "OPTION" };
        String[][] rows = new String [2][2];

        rows[0][0] = "E";
        rows[0][1] = "Edit quantity";

        rows[1][0] = "D";
        rows[1][1] = "Delete order";

        return new TextTable(columnNames, rows);
    }

    private TextTable orderDetailsTable(List<OrderDetailResponse> orderDetails) {
        String[] columnNames = { "NAME", "QUANTITY", "PRICE", "TOTAL PRICE" };
        String[][] rows = new String [ orderDetails.size() + 1][ columnNames.length ];

        int currRow = 0;
        int totalPrice = 0;
        for (OrderDetailResponse orderDetail: orderDetails) {
            rows[currRow][0] = orderDetail.getProductName();
            rows[currRow][1] = String.format("%d pcs", orderDetail.getProductQuantity());
            rows[currRow][2] = String.format("Rp. %,d", orderDetail.getProductPrice());
            rows[currRow][3] = String.format("Rp. %,d", orderDetail.getTotalPrice());

            totalPrice += orderDetail.getTotalPrice();
            currRow++;
        }

        rows[currRow][3] =  String.format("Rp. %,d", totalPrice);

        return new TextTable(columnNames, rows);
    }

    private void productsMenu() {
        try {
            MerchantWithProductsResponse merchantDetails = this.merchantController
                    .getOpenedMerchantDetailById(this.authData.getCurrMerchantId());
            System.out.println("Daftar menu " + merchantDetails.getMerchantName());
            this.merchantDetailsTable(merchantDetails).printTable();
            System.out.println();

            Set<String> options = merchantDetails.getMerchantProducts()
                    .stream().map(p -> String.valueOf(p.getId()))
                    .collect(Collectors.toSet());
            options.add("O");
            options.add("C");

            String chosenOption = this.getOption("option", options);
            if (chosenOption.equals("O")) {
                OrderResponse orderResponse = this.orderController
                        .getOrderWithOrderDetails(this.authData, this.authData.getCurrOrderId());
                System.out.println("--CURRENT ORDER--");
                this.orderDetailsTable(orderResponse.getOrderDetails()).printTable();
                System.out.println();
            } else if (chosenOption.equals("C")) {
                OrderResponse order = this.orderController.getOrderWithOrderDetails(this.authData,
                        this.authData.getCurrOrderId());
                if (!order.getOrderDetails().isEmpty()) {
                    this.orderController.checkoutCurrentOrder(this.authData);
                    this.authData.setCurrMerchantId(0);
                    this.authData.setCurrOrderId(0);
                } else {
                    throw new InputMismatchException("can't checkout empty order");
                }
            } else {
                ProductResponse currProduct = this.merchantController.getProductInMerchant(
                        this.authData, Integer.parseInt(chosenOption));
                this.productDetailTable(currProduct).printTable();
                System.out.println();

                if (this.orderController.isOrderDetailExists(this.authData, currProduct.getId())) {
                    while (true) {
                        this.orderOptionTable().printTable();
                        System.out.println();

                        String editOption = this.getOption("option",
                                new HashSet<>(Arrays.asList("E", "D")));
                        if (editOption.equals("E")) {
                            int quantityInput = this.getQuantityInput();
                            this.orderController.updateOrderDetail(
                                    this.authData, currProduct.getId(), quantityInput);
                            return;
                        } else if (editOption.equals("D")) {
                            this.orderController.deleteOrderDetail(
                                    authData, currProduct.getId());
                            return;
                        }
                    }
                } else {
                    int quantityInput = this.getQuantityInput();
                    this.orderController.addOrderDetail(
                            this.authData, currProduct.getId(), quantityInput);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        // App content
        while (this.appRunning) {
            if (this.authData == null) {
                System.out.println("---LOGIN---");
                this.login();
            }
            if (this.authData.getCurrMerchantId() < 1) {
                System.out.println("---MERCHANTS---");
                this.merchantsMenu();
            } else {
                System.out.println("---PRODUCTS---");
                this.productsMenu();
            }
        }
    }
}
