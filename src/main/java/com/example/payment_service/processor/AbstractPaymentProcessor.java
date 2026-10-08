package com.example.payment_service.processor;

import com.example.payment_service.exception.InvalidPaymentException;
import com.example.payment_service.model.PaymentRequest;

import java.math.BigDecimal;

public abstract class AbstractPaymentProcessor implements PaymentProcessor {

    protected abstract BigDecimal getMaxAmount();

    @Override

    public void validate(PaymentRequest request){
        if(request.amount().compareTo(getMaxAmount()) >0){
            throw new InvalidPaymentException(
                    getMethod()+" payments cannot exceed "+getMaxAmount()
            );
        }
    }
}
