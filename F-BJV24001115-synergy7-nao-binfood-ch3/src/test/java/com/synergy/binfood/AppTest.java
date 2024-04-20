package com.synergy.binfood;

import com.synergy.binfood.repository.Repository;
import org.junit.jupiter.api.BeforeAll;

import com.synergy.binfood.util.exception.InvalidSeederException;

public class AppTest {
    @BeforeAll
    public static void setUp() throws InvalidSeederException {
        // Fill repository from csv data
        Repository.seed(
                System.getProperty("user.dir") + "/seeders",
                "users",
                "merchants",
                "products");
    }
}
