package com.example.payment_service.processor;

import com.example.payment_service.model.PaymentRequest;
import java.math.BigDecimal;


public interface PaymentProcessor {
    String getMethod();
    void validate(PaymentRequest request);
    BigDecimal calculateFee(BigDecimal amount);
}
