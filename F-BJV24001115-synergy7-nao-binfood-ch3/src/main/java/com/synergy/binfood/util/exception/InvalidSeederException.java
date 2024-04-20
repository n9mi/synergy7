package com.synergy.binfood.util.exception;

public class InvalidSeederException extends Exception {
    public InvalidSeederException(String message) {
        super("invalid seeder -> " + message);
    }
}
