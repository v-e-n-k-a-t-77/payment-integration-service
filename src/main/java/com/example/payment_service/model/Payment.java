package com.example.payment_service.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private final Long id;
    private final BigDecimal amount;
    private final BigDecimal fee;
    private final String method;
    private final String merchantId;
    private String status;
    private final LocalDateTime createdAt;

    public Payment(Long id, BigDecimal amount, BigDecimal fee, String method, String merchantId) {
        this.id = id;
        this.amount = amount;
        this.fee = fee;
        this.method = method;
        this.merchantId = merchantId;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getFee() { return fee; }
    public String getMethod() { return method; }
    public String getMerchantId() { return merchantId; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setStatus(String status) { this.status = status; }
}