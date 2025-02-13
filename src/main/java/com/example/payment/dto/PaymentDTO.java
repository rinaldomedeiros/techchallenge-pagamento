package com.example.payment.dto;

import com.example.payment.model.Payment;
import com.example.payment.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDTO {

    private Long id;
    private String orderId;
    private PaymentStatus paymentStatus;
    private BigDecimal orderValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PaymentDTO() {}

    public PaymentDTO(Long id, String orderId, PaymentStatus paymentStatus, BigDecimal orderValue, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
        this.orderValue = orderValue;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Método utilitário para converter de entidade para DTO
    public static PaymentDTO fromEntity(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getOrderId(),
                payment.getPaymentStatus(),
                payment.getOrderValue(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(BigDecimal orderValue) {
        this.orderValue = orderValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
