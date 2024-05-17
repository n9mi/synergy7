package com.synergy.binarfood.setup;

import com.synergy.binarfood.entity.*;
import com.synergy.binarfood.repository.*;
import com.synergy.binarfood.util.random.Randomizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class Setup {
    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Transactional
    public void setup() {
        orderDetailRepository.hardDeleteAll();
        orderRepository.hardDeleteAll();
        productRepository.hardDeleteAll();
        merchantRepository.hardDeleteAll();
        userRepository.hardDeleteAll();

        int numBuyers = 10;
        int numMerchants = 5;
        int minProductPerMerchant = 5;
        int maxProductPerMerchant = 20;
        int minProductPrice = 5000;
        int maxProductPrice = 100000;

        Random random = new Random();

        // Seeds User
        List<User> usersBuyer = new ArrayList<>();
        List<User> usersMerchants = new ArrayList<>();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        for (int i = 1; i <= numBuyers; i++) {
            User newUser = User.builder()
                    .username(String.format("buyer%d", i))
                    .email(String.format("buyer%d@example.com", i))
                    .password(passwordEncoder.encode("password"))
                    .role(Role.BUYER).build();
            userRepository.saveAndFlush(newUser);
            usersBuyer.add(newUser);
        }
        for (int i = 1; i <= numMerchants; i++) {
            User newUser = User.builder()
                    .username(String.format("merchant%d", i))
                    .email(String.format("merchant%d@example.com", i))
                    .password(passwordEncoder.encode("password"))
                    .role(Role.MERCHANT).build();
            userRepository.saveAndFlush(newUser);
            usersMerchants.add(newUser);
        }

        log.info("[SUCCESS] Seeds {} records to users table", usersBuyer.size() + usersMerchants.size());

        // Seeds Merchant
        List<Merchant> merchants = new ArrayList<>();

        for (User uM : usersMerchants) {
            Merchant newMerchant = Merchant.builder()
                    .name("Toko " + uM.getUsername())
                    .location(RandomStringUtils.randomAlphabetic(10))
                    .user(uM)
                    .open(random.nextInt() % 2 == 0).build();
            merchantRepository.saveAndFlush(newMerchant);
            merchants.add(newMerchant);
        }

        log.info("[SUCCESS] Seeds {} records to merchants table", merchants.size());

        // Seeds product
        List<Product> products = new ArrayList<>();

        for (Merchant merchant : merchants) {
            int numProdPerMerchant = Randomizer.generate(minProductPerMerchant, maxProductPerMerchant);
            for (int i = 0; i < numProdPerMerchant; i++) {
                Product newProduct = Product.builder()
                        .name("Product " + RandomStringUtils.randomAlphanumeric(10))
                        .price(Randomizer.generatePrice(minProductPrice, maxProductPrice))
                        .merchant(merchant).build();
                productRepository.saveAndFlush(newProduct);
                products.add(newProduct);
            }
        }

        log.info("[SUCCESS] Seeds {} records to products table", products.size());
        // Seeds order
        List<Order> orders = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (User user: usersBuyer) {
            int randomOrderCount = Randomizer.generate(1, 10);
            for (int c = 0; c < randomOrderCount; c++) {
                Order newOrder = Order.builder()
                        .orderAt(new Date())
                        .destinationAddress(RandomStringUtils.randomAlphabetic(10))
                        .user(user)
                        .completedAt(new Date()).build();
                orderRepository.saveAndFlush(newOrder);
                orders.add(newOrder);

                Merchant merchant = merchants.get(Randomizer.generate(0, numMerchants - 1));
                List<Product> productsOfMerchant = productRepository.findAllByMerchant(merchant, Pageable.unpaged())
                        .stream().toList();

                int randomNumOrderDetail = Randomizer.generate(1, productsOfMerchant.size());
                for (int i = 0; i < randomNumOrderDetail; i++) {
                    int randQuantity = Randomizer.generate(1, 10);
                    OrderDetail newOrderDetail = OrderDetail.builder()
                            .order(newOrder)
                            .product(productsOfMerchant.get(i))
                            .quantity(randQuantity)
                            .totalPrice(randQuantity * productsOfMerchant.get(i).getPrice()).build();
                    orderDetailRepository.saveAndFlush(newOrderDetail);
                    orderDetails.add(newOrderDetail);
                }
            }
        }

        log.info("[SUCCESS] Seeds orders table");
        log.info("[SUCCESS] Seeds order_details table");
    }
}
