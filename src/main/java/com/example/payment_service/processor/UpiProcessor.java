package com.example.payment_service.processor;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UpiProcessor extends AbstractPaymentProcessor {

    @Override
    public String getMethod() {
        return "UPI";
    }

    @Override
    protected BigDecimal getMaxAmount(){
        return new  BigDecimal("100000");
    }

    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return BigDecimal.ZERO;
    }
}
