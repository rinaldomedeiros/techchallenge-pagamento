package com.example.payment.producer;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/simulate")
public class SimulationController {

    private final PaymentProducer paymentProducer;

    public SimulationController(PaymentProducer paymentProducer) {
        this.paymentProducer = paymentProducer;
    }

    /**
     * Endpoint para simular o envio de um evento de pedido.
     * Exemplo: POST /simulate/order?orderId=123&orderValue=100.50
     */
    @PostMapping("/order")
    public ResponseEntity<?> simulateOrder(@RequestParam String orderId,
                                           @RequestParam BigDecimal orderValue) {
        paymentProducer.sendOrderRequestedEvent(orderId, orderValue);
        return ResponseEntity.ok("Evento de pedido enviado para orderId: " + orderId);
    }
}