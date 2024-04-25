package com.synergy.binfood.util.exception;

public class WriterException extends Exception {
    public WriterException(String message) {
        super("can't write receipt -> " + message);
    }
}
