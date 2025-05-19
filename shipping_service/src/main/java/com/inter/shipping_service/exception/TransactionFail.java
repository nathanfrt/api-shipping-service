package com.inter.shipping_service.exception;

public class TransactionFail extends RuntimeException {
    public TransactionFail(String message) {
        super(message);
    }
}
