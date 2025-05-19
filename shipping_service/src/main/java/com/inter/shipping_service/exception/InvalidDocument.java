package com.inter.shipping_service.exception;

public class InvalidDocument extends RuntimeException {
    public InvalidDocument(String message) {
        super(message);
    }
}
