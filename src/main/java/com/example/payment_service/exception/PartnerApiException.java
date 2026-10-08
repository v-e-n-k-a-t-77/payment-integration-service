package com.example.payment_service.exception;

public class PartnerApiException extends RuntimeException {
    public PartnerApiException(String message) {
        super(message);
    }
}