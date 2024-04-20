package com.synergy.binfood.util.exception;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super("validation error -> " + message);
    }
}
