package com.synergy.binfood.utils;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ExceptionUtil<T> {
    public static <T> String getViolationsMessage(Set<ConstraintViolation<T>> violations) {
        StringBuilder messageBuilder = new StringBuilder();
        violations.forEach(e ->messageBuilder.append(e.getMessage()).append("\n"));

        return  messageBuilder.toString();
    }
}
