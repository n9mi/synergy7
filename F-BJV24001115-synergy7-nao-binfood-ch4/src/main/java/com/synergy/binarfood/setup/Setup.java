package com.synergy.binarfood.setup;

import com.synergy.binarfood.entity.*;
import com.synergy.binarfood.repository.*;
import com.synergy.binarfood.util.random.Randomizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
public class Setup {
    @Autowired
    UserRepository userRepository;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Transactional
    public void setup() {
        orderDetailRepository.hardDeleteAll();
        orderRepository.hardDeleteAll();
        productRepository.hardDeleteAll();
        merchantRepository.hardDeleteAll();
        userRepository.hardDeleteAll();

        int numUsers = 10;
        int numMerchants = 5;
        int minProductPerMerchant = 5;
        int maxProductPerMerchant = 20;
        int minProductPrice = 5000;
        int maxProductPrice = 100000;

        Random random = new Random();

        // Seeds User
        List<User> users = new ArrayList<>();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        for (int i = 1; i <= numUsers; i++) {
            User newUser = User.builder()
                    .username(RandomStringUtils.randomAlphabetic(10))
                    .email(RandomStringUtils.randomAlphabetic(10) + "@example.com")
                    .password(passwordEncoder.encode("password"))
                    .token(UUID.randomUUID().toString()).build();
            userRepository.saveAndFlush(newUser);
            users.add(newUser);
        }

        log.info("[SUCCESS] Seeds {} records to users table", users.size());

        // Seeds Merchant
        List<Merchant> merchants = new ArrayList<>();

        for (int i = 1; i <= numMerchants; i++) {
            Merchant newMerchant = Merchant.builder()
                    .name("Merchant " + String.valueOf(i))
                    .location(RandomStringUtils.randomAlphabetic(10))
                    .isOpen(random.nextInt() % 2 == 0).build();
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

        for (User user: users) {
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
