package com.example.payment.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentScheduler {

    private final PaymentService paymentService;

    public PaymentScheduler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @Scheduled(fixedRate = 60000)
    public void checkExpiredPayments() {
        paymentService.updateExpiredPayments();
    }
}
