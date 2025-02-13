package com.example.payment.dto;

public class OrderPaidMessageDTO {

    private String orderId;
    private String paymentStatus;

    public OrderPaidMessageDTO() {
    }

    public OrderPaidMessageDTO(String orderId, String paymentStatus) {
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
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
