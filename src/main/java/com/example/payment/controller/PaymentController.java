package com.example.payment.controller;

import com.example.payment.dto.PaymentDTO;
import com.example.payment.model.Payment;
import com.example.payment.model.PaymentStatus;
import com.example.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<PaymentDTO> confirmPayment(@PathVariable String orderId) {
        try {
            Payment payment = paymentService.confirmPayment(orderId);
            PaymentDTO dto = PaymentDTO.fromEntity(payment);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(@RequestParam(required = false) String status) {
        try {
            List<Payment> payments;

            if (status != null && !status.isEmpty()) {
                PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
                payments = paymentService.getPaymentsByStatus(paymentStatus);
            } else {
                payments = paymentService.getAllPayments();
            }

            List<PaymentDTO> dtos = payments.stream()
                    .map(PaymentDTO::fromEntity)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }
}
