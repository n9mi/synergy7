package com.synergy.binfood.util.exception;

public class UnauthorizedException extends Exception {
    public UnauthorizedException(String message) {
        super("unauthorized error -> " + message);
    }
}
