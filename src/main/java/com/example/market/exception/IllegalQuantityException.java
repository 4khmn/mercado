package com.example.market.exception;

public class IllegalQuantityException extends RuntimeException {
    public IllegalQuantityException(String message) {
        super(message);
    }
}
