package com.example.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderRequestedMessageDTO {

    private String orderId;
    private String paymentStatus;
    private BigDecimal orderValue;
    private LocalDateTime timestamp;

    public OrderRequestedMessageDTO() {
    }

    public OrderRequestedMessageDTO(String orderId, String paymentStatus, BigDecimal orderValue, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
        this.orderValue = orderValue;
        this.timestamp = timestamp;
    }

    // Getters e Setters
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public BigDecimal getOrderValue() {
        return orderValue;
    }
    public void setOrderValue(BigDecimal orderValue) {
        this.orderValue = orderValue;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
