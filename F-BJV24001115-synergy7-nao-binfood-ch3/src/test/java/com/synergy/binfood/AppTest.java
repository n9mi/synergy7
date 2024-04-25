package com.synergy.binfood;

import com.synergy.binfood.repository.Repository;
import com.synergy.binfood.service.Service;
import jakarta.validation.Validation;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
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

        // Setup service
        Service.setValidator(Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator());
    }
}
