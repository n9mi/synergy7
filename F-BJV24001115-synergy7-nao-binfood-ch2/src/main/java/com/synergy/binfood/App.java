package com.synergy.binfood;

import com.synergy.binfood.controller.RestaurantController;
import com.synergy.binfood.repository.*;
import com.synergy.binfood.service.*;
import com.synergy.binfood.utils.SeederUtil;
import com.synergy.binfood.utils.exception.SeederError;
import com.synergy.binfood.view.RestaurantView;

public class App 
{
    public static void main( String[] args )
    {
        // Seeds repository
        SeederUtil seederUtil = new SeederUtil(System.getProperty("user.dir") + "/seeds", "menu.csv",
                "variant.csv");
        Repository repository = new Repository();
        try {
            repository.seed(seederUtil);
        } catch (SeederError seederError) {
            System.out.println("Failed to seed : " + seederError.getMessage());
        }

        // Setup controller and view
        RestaurantController restaurantController = getRestaurantController();
        RestaurantView restaurantView = new RestaurantView(restaurantController);
        restaurantView.run();
    }

    private static RestaurantController getRestaurantController() {
        // Setup repository and seeder
        MenuRepository menuRepository = new MenuRepository();
        VariantRepository variantRepository = new VariantRepository();
        OrderRepository orderRepository = new OrderRepository();
        OrderItemRepository orderItemRepository = new OrderItemRepository();

        // Setup validator and services
        MenuService menuService = new MenuServiceImpl(menuRepository, variantRepository);
        OrderService orderService = new OrderServiceImpl(orderRepository, orderItemRepository,
                variantRepository, menuRepository);

        // Setup controller
        return new RestaurantController(menuService, orderService);
    }
}
