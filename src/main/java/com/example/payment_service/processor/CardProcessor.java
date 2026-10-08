package com.example.payment_service.processor;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CardProcessor extends AbstractPaymentProcessor {

    @Override
    public String getMethod() {
        return "CARD";
    }

    @Override
    protected BigDecimal getMaxAmount() {
        return new BigDecimal("200000");
    }

    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return amount.multiply(new BigDecimal("0.02"))
                .setScale(2, RoundingMode.HALF_UP);
    }
}