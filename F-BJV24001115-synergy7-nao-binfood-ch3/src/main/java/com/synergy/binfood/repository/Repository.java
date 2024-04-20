package com.synergy.binfood.repository;

import com.synergy.binfood.entity.*;
import com.synergy.binfood.util.exception.InvalidSeederException;
import com.synergy.binfood.util.random.Randomizer;
import com.synergy.binfood.util.seeder.CSVSeeder;
import com.synergy.binfood.util.seeder.data.ProductSeedData;
import org.apache.commons.lang.mutable.MutableInt;

import java.util.*;
import java.util.stream.Collectors;

public class Repository {
    protected static Map<Integer, User> users;
    protected static Map<Integer, Merchant> merchants;
    protected static Map<Integer, Product> products;
    protected static Map<Integer, Order> orders = new HashMap<>();
    protected static Map<Integer, OrderDetail> orderDetails = new HashMap<>();

    public static void seed(String folderPath, String userSeederFileName, String merchantSeederFileName,
                            String productSeederFileName) throws InvalidSeederException  {
        CSVSeeder csvSeeder = new CSVSeeder(folderPath, userSeederFileName, merchantSeederFileName,
                productSeederFileName);

        // Seed users with user's seeder data
        csvSeeder.seedUsers();
        MutableInt userIDCounter = new MutableInt(0);
        Repository.users = csvSeeder.getUserSeedData().stream()
                .map(userSeedData -> {
                    userIDCounter.increment();
                    return new User(userIDCounter.intValue(),
                            userSeedData.getUsername(),
                            userSeedData.getEmailAddress(),
                            userSeedData.getPassword());
                }).collect(Collectors.toMap(User::getId, u -> u));

        // Seed merchant with merchant's seeder data
        csvSeeder.seedMerchants();
        MutableInt merchantIDCounter = new MutableInt(0);
        Repository.merchants = csvSeeder.getMerchantSeedData().stream()
                .map(merchantSeedData -> {
                    merchantIDCounter.increment();
                    return new Merchant(merchantIDCounter.intValue(),
                            merchantSeedData.getMerchantName(),
                            merchantSeedData.getMerchantLocation(),
                            merchantSeedData.isMerchantOpen());
                }).collect(Collectors.toMap(Merchant::getId, m -> m));

        // Seed merchant products
        Repository.products = new HashMap<>();
        csvSeeder.seedProducts();
        MutableInt productIdCounter = new MutableInt(0);
        List<ProductSeedData> productSeedData = csvSeeder.getProductSeedData();
        for (Merchant merchant: Repository.merchants.values()) {
            Set<Integer> currProductDataIndex = new HashSet<>();
            for (int i = 0; i < 5; i++) {
                int randomProductDataIndex = Randomizer.generate(0, productSeedData.size() - 1);
                while (currProductDataIndex.contains(randomProductDataIndex)) {
                    randomProductDataIndex = Randomizer.generate(0, productSeedData.size() - 1);
                }
                currProductDataIndex.add(randomProductDataIndex);
                ProductSeedData randomProductSeedData = productSeedData.get(randomProductDataIndex);

                productIdCounter.increment();
                Repository.products.put(productIdCounter.intValue(),
                        new Product(productIdCounter.intValue(),
                                randomProductSeedData.getProductName(),
                                Randomizer.generatePrice(randomProductSeedData.getPriceLowerBound(),
                                        randomProductSeedData.getPriceHigherBound()),
                                merchant.getId()));
            }
            currProductDataIndex.clear();
        }
    }
}
