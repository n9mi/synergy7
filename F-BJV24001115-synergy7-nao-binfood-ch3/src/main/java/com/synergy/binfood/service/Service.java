package com.synergy.binfood.service;

import jakarta.validation.Validator;

public class Service {
    protected static Validator validator;

    public static void setValidator(Validator validator) {
        Service.validator = validator;
    }
}
