package com.example.payment.dto;

import com.example.payment.model.Payment;
import com.example.payment.model.PaymentStatus;

public class OrderPaidMessageDTO {

    private String orderId;
    private String paymentStatus;

    public OrderPaidMessageDTO() {
    }

    public OrderPaidMessageDTO(String orderId, String paymentStatus) {
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
    }

    public OrderPaidMessageDTO(Payment payment) {
        this.orderId = payment.getOrderId();
        this.paymentStatus = payment.getPaymentStatus() == PaymentStatus.CONFIRMADO
                ? "APPROVED"
                : payment.getPaymentStatus().name();
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
}
