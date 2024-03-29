package com.synergy.binfood.controller;

import com.synergy.binfood.model.menu.*;
import com.synergy.binfood.model.order.*;
import com.synergy.binfood.service.MenuService;
import com.synergy.binfood.service.OrderService;
import com.synergy.binfood.utils.exception.DuplicateItemError;
import com.synergy.binfood.utils.exception.NotFoundError;
import com.synergy.binfood.utils.exception.ValidationError;
import dnl.utils.text.table.TextTable;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class RestaurantController {
    private final MenuService menuService;
    private final OrderService orderService;

    public OrderIdResponse createOrder() {
        return this.orderService.create();
    }

    public TextTable getMenus() {
        List<MenuResponse> menus = this.menuService.findMenus();

        String[] columnNames = { "CODE", "MENU", "PRICE" };

        String[][] menusData = new String[menus.size() + 3][3];
        int row = 0;
        for (MenuResponse menu : menus) {
            menusData[row][0] = menu.getMenuCode();
            menusData[row][1] = menu.getMenuName();
            menusData[row][2] = String.format("Rp. %,d", menu.getMenuPrice());
            row++;
        }

        menusData[row][0] = "O";
        menusData[row][1] = "Get list of orders";
        menusData[row][2] = "";

        menusData[row + 1][0] = "P";
        menusData[row + 1][1] = "Pay";
        menusData[row + 1][2] = "";

        menusData[row + 2][0] = "X";
        menusData[row + 2][1] = "Stop app";
        menusData[row + 2][2] = "";

        return new TextTable(columnNames, menusData);
    }

    public boolean checkMenuHasVariants(String menuCode) throws Exception {
        try {
            return this.menuService.isMenuHasAnyVariant(new MenuIdRequest(menuCode));
        } catch (ValidationError validationError) {
            String errMsg = "Validation error : " + validationError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (NotFoundError notFoundError) {
            String errMsg = "Not found error : " + notFoundError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (Exception exception) {
            String errMsg = "Something wrong : " + exception.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        }
    }

    public boolean checkMenuHasVariant(String menuCode, String variantCode) throws Exception {
        try {
            return this.menuService.isMenuHasVariant(new MenuVariantRequest(menuCode, variantCode));
        } catch (ValidationError validationError) {
            String errMsg = "Validation error : " + validationError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (NotFoundError notFoundError) {
            String errMsg = "Not found error : " + notFoundError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (Exception exception) {
            String errMsg = "Something wrong : " + exception.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        }
    }

    public TextTable getMenuWithVariants(String menuCode) throws Exception {
        try {
            MenuWithVariantRequest request = new MenuWithVariantRequest(menuCode, null);

            String[] columnNames = { "CODE", "MENU/VARIANT NAME", "PRICE" };
            MenuWithVariantsResponse menuWithVariants = this.menuService.findMenuWithVariants(request);

            String[][] menuVariantsData = new String[menuWithVariants.getVariantResponses().size() + 2][3];
            int row = 0;
            menuVariantsData[row][0] = menuWithVariants.getMenuCode();
            menuVariantsData[row][1] = menuWithVariants.getMenuName();
            menuVariantsData[row][2] = String.format("Rp. %,d", menuWithVariants.getMenuPrice());
            row++;
            for (VariantResponse variantResponse: menuWithVariants.getVariantResponses()) {
                menuVariantsData[row][0] = variantResponse.getCode();
                menuVariantsData[row][1] = variantResponse.getName();
                menuVariantsData[row][2] = "";
                row ++;
            }
            menuVariantsData[row][0] = "X";
            menuVariantsData[row][1] = "Back to menu";
            menuVariantsData[row][2] = "";

            return new TextTable(columnNames, menuVariantsData);
        } catch (ValidationError validationError) {
            String errMsg = "Validation error : " + validationError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (NotFoundError notFoundError) {
            String errMsg = "Not found error : " + notFoundError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (Exception exception) {
            String errMsg = "Something wrong : " + exception.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        }
    }

    public CheckIsOrderItemExistsResponse checkIsOrderItemExists(String orderId, String menuCode, String variantCode) {
        OrderItemRequest orderItemRequest = new OrderItemRequest(orderId, menuCode, variantCode, 0);

        return this.orderService.checkIsOrderItemOnOrderExists(orderItemRequest);
    }

    public TextTable getOrderedItems(String orderId) throws Exception {
        try {
            OrderResponse orderResponse = this.orderService.find(new GetOrderRequest(orderId));
            String[] columnNames = { "MENU CODE", "MENU NAME", "VARIANT CODE", "VARIANT NAME", "QUANTITY" };
            String[][] orderItemsData = new String[orderResponse.getOrderItems().size()][5];

            int row = 0;
            for (OrderItemResponse orderItemResponse: orderResponse.getOrderItems()) {
                orderItemsData[row][0] = orderItemResponse.getMenuCode();
                orderItemsData[row][1] = orderItemResponse.getMenuName();
                orderItemsData[row][2] = orderItemResponse.getVariantCode();
                orderItemsData[row][3] = orderItemResponse.getVariantName();
                orderItemsData[row][4] = String.format("%d pcs", orderItemResponse.getQuantity());
                row++;
            }

            return new TextTable(columnNames, orderItemsData);
        } catch (ValidationError validationError) {
            String errMsg = "Validation error : " + validationError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (NotFoundError notFoundError) {
            String errMsg = "Not found error : " + notFoundError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (Exception exception) {
            String errMsg = "Something wrong : " + exception.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        }
    }

    public TextTable getOrderedItem(String orderId, String menuCode, String variantCode) throws Exception {
        try {
            OrderItemRequest orderItemRequest = new OrderItemRequest(orderId, menuCode, variantCode, 0);
            String[] columnNames = { "CODE", "MENU/VARIANT NAME", "QUANTITY" };
            OrderItemResponse orderItemResponse = this.orderService.findOrderItemFromOrder(orderItemRequest);

            String[][] orderItemData = new String[4][3];
            orderItemData[0][0] = orderItemResponse.getMenuCode();
            orderItemData[0][1] = orderItemResponse.getMenuName();
            orderItemData[0][2] = "";

            orderItemData[1][0] = orderItemResponse.getVariantCode();
            orderItemData[1][1] = orderItemResponse.getVariantName();
            orderItemData[1][2] = String.format("%d pcs", orderItemResponse.getQuantity());

            orderItemData[2][0] = "E";
            orderItemData[2][1] = "Edit quantity";
            orderItemData[2][2] = "";

            orderItemData[3][0] = "D";
            orderItemData[3][1] = "Delete this item";
            orderItemData[3][2] = "";

            return new TextTable(columnNames, orderItemData);
        } catch (NotFoundError notFoundError) {
            String errMsg = "Not found error : " + notFoundError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (Exception exception) {
            String errMsg = "Something wrong : " + exception.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        }
    }


    public void createOrderItem(String orderId, String menuCode, String variantCode, int quantity) throws Exception {
        OrderItemRequest orderItemRequest = new OrderItemRequest(orderId, menuCode, variantCode, quantity);

        try {
            this.orderService.createOrderItem(orderItemRequest);
        } catch (ValidationError validationError) {
            String errMsg = "Validation error : " + validationError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (NotFoundError notFoundError) {
            String errMsg = "Not found error : " + notFoundError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (DuplicateItemError duplicateItemError) {
            String errMsg = "Duplicate item : " + duplicateItemError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (Exception exception) {
            String errMsg = "Something wrong : " + exception.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        }
    }

    public void updateOrderItem(String orderId, String menuCode, String variantCode, int quantity) throws Exception {
        OrderItemRequest orderItemRequest = new OrderItemRequest(orderId, menuCode, variantCode, quantity);

        try {
            this.orderService.updateOrderItem(orderItemRequest);
        } catch (ValidationError validationError) {
            String errMsg = "Validation error : " + validationError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (NotFoundError notFoundError) {
            String errMsg = "Not found error : " + notFoundError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (Exception exception) {
            String errMsg = "Something wrong : " + exception.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        }
    }

    public void deleteOrderItem(String orderId, String menuCode, String variantCode) throws Exception {
        DeleteOrderItemRequest orderItemRequest = new DeleteOrderItemRequest(orderId, menuCode, variantCode);

        try {
            this.orderService.deleteOrderItem(orderItemRequest);
        } catch (ValidationError validationError) {
            String errMsg = "Validation error : " + validationError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (NotFoundError notFoundError) {
            String errMsg = "Not found error : " + notFoundError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (Exception exception) {
            String errMsg = "Something wrong : " + exception.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        }
    }

    public void payOrder(String orderId) throws Exception {
        try {
            this.orderService.pay(new GetOrderRequest(orderId));
        } catch (ValidationError validationError) {
            String errMsg = "Validation error : " + validationError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (NotFoundError notFoundError) {
            String errMsg = "Not found error : " + notFoundError.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        } catch (Exception exception) {
            String errMsg = "Something wrong : " + exception.getMessage();
            System.out.println(errMsg);
            throw new Exception(errMsg);
        }
    }
}
