package com.example.payment_service.controller;

import com.example.payment_service.model.Payment;
import com.example.payment_service.model.PaymentRequest;
import com.example.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Payment create(@Valid @RequestBody PaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @GetMapping("/{id}")
    public Payment get(@PathVariable Long id) {
        return paymentService.getPayment(id);
    }

    @GetMapping
    public List<Payment> getAll() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}/status")
    public Payment checkStatus(@PathVariable Long id) {
        return paymentService.refreshStatus(id);
    }
}