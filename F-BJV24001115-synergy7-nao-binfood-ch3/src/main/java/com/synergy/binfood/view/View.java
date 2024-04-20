package com.synergy.binfood.view;

import com.synergy.binfood.config.Bootstrap;
import com.synergy.binfood.controller.AuthController;
import com.synergy.binfood.controller.MerchantController;
import com.synergy.binfood.controller.OrderController;

public class View {
    private AuthController authController;
    private MerchantController merchantController;
    private OrderController orderController;

    public View(Bootstrap bootstrap) {
        this.authController = bootstrap.authController;
        this.merchantController = bootstrap.merchantController;
        this.orderController = bootstrap.orderController;
    }

    public void run() {
        // App content
    }
}
