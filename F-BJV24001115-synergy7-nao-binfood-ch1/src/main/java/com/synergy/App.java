package com.synergy;

import com.synergy.controller.RestaurantController;
import com.synergy.model.Receipt;
import com.synergy.repository.MenuRepository;
import com.synergy.repository.TransactionRepository;
import com.synergy.service.RestaurantService;
import com.synergy.service.TransactionService;
import com.synergy.utils.ReceiptWriter;

public class App
{
    public static void main( String[] args )
    {
        // Setup repository
        MenuRepository menuRepository = new MenuRepository(System.getProperty("user.dir") + "/menu.csv");
        TransactionRepository transactionRepository = new TransactionRepository();

        // Setup service
        RestaurantService restaurantService = new RestaurantService(menuRepository, transactionRepository);
        TransactionService transactionService = new TransactionService(transactionRepository, menuRepository);

        // Setup controller
        RestaurantController restaurantController = new RestaurantController("Binar food", restaurantService, transactionService);
        restaurantController.run();
    }
}
