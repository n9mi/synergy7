package com.synergy.binfood;

import com.synergy.binfood.entity.OrderDetail;
import com.synergy.binfood.entity.Product;
import com.synergy.binfood.repository.*;
import com.synergy.binfood.service.OrderDetailService;
import com.synergy.binfood.service.OrderDetailServiceImpl;
import com.synergy.binfood.service.OrderService;
import com.synergy.binfood.service.OrderServiceImpl;
import com.synergy.binfood.controller.OrderController;
import com.synergy.binfood.model.auth.AuthData;

import com.synergy.binfood.util.exception.DuplicateItemException;
import com.synergy.binfood.util.exception.NotFoundException;
import com.synergy.binfood.util.exception.UnauthorizedException;
import com.synergy.binfood.util.exception.ValidationException;
import com.synergy.binfood.util.random.Randomizer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderTest extends AppTest {
    private final OrderRepository orderRepository = new OrderRepository();
    private final UserRepository userRepository = new UserRepository();
    private final MerchantRepository merchantRepository = new MerchantRepository();
    private final OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
    private final ProductRepository productRepository = new ProductRepository();

    private final OrderService orderService = new OrderServiceImpl(orderRepository, userRepository, merchantRepository);
    private final OrderDetailService orderDetailService = new OrderDetailServiceImpl(
            orderDetailRepository, orderRepository, userRepository, merchantRepository, productRepository);

    private final OrderController orderController = new OrderController(orderService, orderDetailService);

    // VALID or INVALID auth data depends on user seeder
    // VALID means user exists in seeder
    // INVALID means user doesn't exists in seeder
    private AuthData VALID_authData = new AuthData(1, "naomi", 1);

    @DisplayName("ORDER TEST - CREATE - SUCCESS")
    @Test
    @Order(1)
    void createOrderSuccess() {
        assertDoesNotThrow(() -> this.VALID_authData = this.orderController.create(
                VALID_authData, "Jl. Jalan-jalan"));
    }

    private List<AuthData> getInvalidAuthData() {
        return List.of(
                new AuthData(1, "joko", 1),
                new AuthData(99, "naomi", 1),
                new AuthData(99, "joko", 1),
                new AuthData(0, null, 0)
        );
    }

    @DisplayName("ORDER TEST - CREATE - FAILED -> UNAUTHORIZED")
    @ParameterizedTest()
    @MethodSource({"getInvalidAuthData"})
    void createOrderFailedUnauthorized(AuthData invalidAuthData) {
        assertThrows(UnauthorizedException.class, () ->
                this.orderController.create(invalidAuthData, "Jl. Jalan-jalan"));
    }

    @DisplayName("ORDER TEST - CREATE - FAILED -> VALIDATION ERROR")
    @Test
    void createOrderFailedValidation() {
        assertThrows(ValidationException.class, () ->
                this.orderController.create(this.VALID_authData, ""));
        assertThrows(ValidationException.class, () ->
                this.orderController.create(this.VALID_authData, null));
    }

    private List<AuthData> getInvalidAuthMerchant() {
        return List.of(
                new AuthData(1, "naomi", -1),
                new AuthData(1, "naomi", 99),
                new AuthData(1, "naomi", 0)
        );
    }

    @DisplayName("ORDER TEST - CREATE - FAILED -> MERCHANT NOT FOUND")
    @ParameterizedTest()
    @MethodSource({ "getInvalidAuthMerchant" })
    void createOrderFailedMerchantNotFound(AuthData authData) {
        try {
            assertThrows(NotFoundException.class, () ->
                    this.orderController.create(authData, "Jl. Suka Jalan"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @DisplayName("ORDER TEST - ADD ORDER DETAIL - FAILED -> INVALID QUANTITY")
    @ParameterizedTest
    @ValueSource(ints = { -1, 0 })
    @Order(2)
    void addOrderDetailFailedInvalidQuantity(int quantity) {
        List<Product> merchantProduct = merchantRepository.findProducts(this.VALID_authData.getCurrMerchantId());
        Product randomProduct = merchantProduct.get(Randomizer.generate(0, merchantProduct.size() - 1));

        assertThrows(ValidationException.class, () -> this.orderController.addOrderDetail(this.VALID_authData,
                randomProduct.getId(), quantity));
    }

    @DisplayName("ORDER TEST - ADD ORDER DETAIL - SUCCESS")
    @Test
    @Order(3)
    void addOrderDetailSuccess() {
        List<Product> merchantProduct = merchantRepository.findProducts(this.VALID_authData.getCurrMerchantId());
        Product randomProduct = merchantProduct.get(Randomizer.generate(0, merchantProduct.size() - 1));

        assertDoesNotThrow(() -> this.orderController.addOrderDetail(this.VALID_authData,
                randomProduct.getId(), 10));
    }

    private List<AuthData> getInvalidAuthMerchantWithValidOrder() {
        return List.of(
                new AuthData(1, "naomi", -1, this.VALID_authData.getCurrOrderId()),
                new AuthData(1, "naomi", 0, this.VALID_authData.getCurrOrderId()),
                new AuthData(1, "naomi", 99, this.VALID_authData.getCurrOrderId())
        );
    }

    @DisplayName("ORDER TEST - ADD ORDER DETAIL - FAILED -> MERCHANT NOT FOUND")
    @ParameterizedTest()
    @MethodSource({ "getInvalidAuthMerchantWithValidOrder" })
    void addOrderDetailInvalidMerchant(AuthData authData) {
        assertThrows(NotFoundException.class, () -> this.orderController.addOrderDetail(authData,
                1, 10));
    }


    private List<AuthData> getInvalidAuthOrder() {
        return List.of(
                new AuthData(1, "naomi", 1, -1),
                new AuthData(1, "naomi", 1, 0),
                new AuthData(1, "naomi", 1, 99)
        );
    }

    @DisplayName("ORDER TEST - ADD ORDER DETAIL - FAILED -> UNAUTHORIZED ORDER")
    @ParameterizedTest()
    @MethodSource({ "getInvalidAuthOrder" })
    void addOrderDetailUnauthorizedOrder(AuthData authData) {
        assertThrows(UnauthorizedException.class, () -> this.orderController.addOrderDetail(authData,
                1, 10));
    }

    public List<OrderDetail> getExistingOrderDetail() {
        return this.orderDetailRepository.findAllByOrderId(this.VALID_authData.getCurrOrderId());
    }

    @DisplayName("ORDER TEST - ADD ORDER DETAIL - FAILED -> DUPLICATE ORDER")
    @ParameterizedTest
    @MethodSource({ "getExistingOrderDetail" })
    @Order(4)
    void addOrderDetailFailedDuplicate(OrderDetail existingOrderDetail) {
        assertThrows(DuplicateItemException.class, () -> this.orderController.addOrderDetail(this.VALID_authData,
                existingOrderDetail.getProductId(), 10));
    }

    @DisplayName("ORDER TEST - CHECK ORDER DETAIL EXISTS - SUCCESS")
    @ParameterizedTest
    @MethodSource({"getExistingOrderDetail"})
    @Order(5)
    void checkOrderDetailExistsSuccess(OrderDetail existingOrderDetail) {
        assertTrue(this.orderController.isOrderDetailExists(this.VALID_authData, existingOrderDetail.getProductId()));
    }

    @DisplayName("ORDER TEST - UPDATE ORDER - SUCCESS")
    @ParameterizedTest
    @MethodSource({ "getExistingOrderDetail" })
    @Order(6)
    void updateOrderDetailSuccess(OrderDetail existingOrderDetail) {
        assertDoesNotThrow(() -> this.orderController.updateOrderDetail(this.VALID_authData,
                existingOrderDetail.getProductId(), 100));
    }

    @DisplayName("ORDER TEST - UPDATE ORDER - FAILED -> VALIDATION ERROR")
    @ParameterizedTest
    @ValueSource(ints = { -99, 0 })
    @Order(7)
    void updateOrderDetailFailedValidationError(int productId) {
        assertThrows(ValidationException.class, () ->
                this.orderController.updateOrderDetail(this.VALID_authData, productId, 100));
    }

    @DisplayName("ORDER TEST - UPDATE ORDER - FAILED -> PRODUCT NOT FOUND")
    @ParameterizedTest
    @ValueSource(ints = { 100, 200 })
    @Order(8)
    void updateOrderDetailFailedProductNotFound(int productId) {
        assertThrows(NotFoundException.class, () ->
                this.orderController.updateOrderDetail(this.VALID_authData, productId, 100));
    }

    @DisplayName("ORDER TEST - DELETE ORDER - SUCCESS")
    @ParameterizedTest
    @MethodSource({ "getExistingOrderDetail" })
    @Order(9)
    void deleteOrderDetailSuccess(OrderDetail existingOrderDetail) {
        assertDoesNotThrow(() -> this.orderController.deleteOrderDetail(this.VALID_authData,
                existingOrderDetail.getProductId()));
    }

    @DisplayName("ORDER TEST - DELETE ORDER - FAILED -> VALIDATION ERROR")
    @ParameterizedTest
    @ValueSource(ints = { -1, 0})
    @Order(10)
    void deleteOrderDetailFailedValidationError(int productId) {
        assertThrows(ValidationException.class, () ->
                this.orderController.updateOrderDetail(this.VALID_authData, productId, 100));
    }

    @DisplayName("ORDER TEST - DELETE ORDER - FAILED -> PRODUCT NOT FOUND")
    @ParameterizedTest
    @ValueSource(ints = { 100, 200 })
    @Order(10)
    void deleteOrderDetailFailedProductNotFound(int productId) {
        assertThrows(NotFoundException.class, () ->
                this.orderController.updateOrderDetail(this.VALID_authData, productId, 100));
    }
}


