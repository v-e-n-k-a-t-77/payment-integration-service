package com.example.payment_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentRequest (
    @NotNull @Positive BigDecimal amount,
    @NotBlank String method,
    @NotBlank String merchantId)
{}
