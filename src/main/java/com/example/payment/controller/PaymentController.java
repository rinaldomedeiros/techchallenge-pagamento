package com.example.payment.controller;

import com.example.payment.model.Payment;
import com.example.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Endpoint para simular a confirmação do pagamento via webhook do QR Code.
     * Exemplo: POST /payment/{orderId}/confirm
     */
    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<?> confirmPayment(@PathVariable String orderId) {
        try {
            Payment payment = paymentService.confirmPayment(orderId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
