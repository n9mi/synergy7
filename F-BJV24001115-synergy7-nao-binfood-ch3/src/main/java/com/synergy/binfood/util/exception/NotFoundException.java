package com.synergy.binfood.util.exception;

public class NotFoundException extends Exception {
    public NotFoundException(String name) {
        super("not found error -> " + name);
    }
}
