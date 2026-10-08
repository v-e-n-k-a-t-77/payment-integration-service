package com.example.payment_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedPaymentMethodException extends RuntimeException {
    public UnsupportedPaymentMethodException(String method) {
        super("Unsupported payment method: " + method);
    }
}
