package com.synergy.binfood.config;

import com.synergy.binfood.controller.AuthController;
import com.synergy.binfood.controller.MerchantController;
import com.synergy.binfood.controller.OrderController;
import com.synergy.binfood.repository.*;
import com.synergy.binfood.service.*;
import jakarta.validation.Validation;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

public class Bootstrap {
    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    private final AuthService authService;
    private final MerchantService merchantService;
    private final OrderService orderService;

    public final AuthController authController;
    public final MerchantController merchantController;
    public final OrderController orderController;

    public Bootstrap() {
        // Fill repository from csv data
        try {
            Repository.seed(
                    System.getProperty("user.dir") + "/seeders",
                    "users",
                    "merchants",
                    "products");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        // Setup repository
        this.userRepository = new UserRepository();
        this.merchantRepository = new MerchantRepository();
        this.orderRepository = new OrderRepository();
        this.orderDetailRepository = new OrderDetailRepository();
        this.productRepository = new ProductRepository();

        // Setup service
        Service.setValidator(Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator());

        this.authService = new AuthServiceImpl(userRepository);
        this.merchantService = new MerchantServiceImpl(merchantRepository, productRepository, userRepository);
        this.orderService = new OrderServiceImpl(orderRepository,
                orderDetailRepository,
                userRepository,
                merchantRepository,
                productRepository);

        // Setup controller
        this.authController = new AuthController(authService);
        this.merchantController = new MerchantController(merchantService);
        this.orderController = new OrderController(orderService);
    }
}
