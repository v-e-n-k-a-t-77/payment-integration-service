package com.example.payment_service.service;

import com.example.payment_service.client.PartnerApiClient;
import com.example.payment_service.exception.PaymentNotFoundException;
import com.example.payment_service.exception.UnsupportedPaymentMethodException;
import com.example.payment_service.model.Payment;
import com.example.payment_service.model.PaymentRequest;
import com.example.payment_service.processor.PaymentProcessor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PaymentService {

    private final Map<Long, Payment> payments = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);
    private final Map<String, PaymentProcessor> processors = new HashMap<>();
    private final PartnerApiClient partnerApiClient;

    public PaymentService(List<PaymentProcessor> processorList,
                          PartnerApiClient partnerApiClient) {
        this.partnerApiClient = partnerApiClient;
        for (PaymentProcessor processor : processorList) {
            processors.put(processor.getMethod(), processor);
        }
    }

    public Payment createPayment(PaymentRequest request) {
        PaymentProcessor processor = processors.get(request.method().toUpperCase());
        if (processor == null) {
            throw new UnsupportedPaymentMethodException(request.method());
        }

        processor.validate(request);
        BigDecimal fee = processor.calculateFee(request.amount());

        Payment payment = new Payment(
                nextId.getAndIncrement(),
                request.amount(),
                fee,
                processor.getMethod(),
                request.merchantId());
        payments.put(payment.getId(), payment);
        return payment;
    }

    public Payment getPayment(Long id) {
        Payment payment = payments.get(id);
        if (payment == null) {
            throw new PaymentNotFoundException(id);
        }
        return payment;
    }

    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments.values());
    }

    public Payment refreshStatus(Long id) {
        Payment payment = getPayment(id);
        String newStatus = partnerApiClient.fetchStatus(id);
        payment.setStatus(newStatus);
        return payment;
    }
}