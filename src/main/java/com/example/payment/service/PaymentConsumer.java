package com.example.payment.service;

import com.example.payment.config.RabbitMQConfig;
import com.example.payment.model.Payment;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class PaymentConsumer {

    private final PaymentService paymentService;

    public PaymentConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Método que consome mensagens da fila ORDER_REQUESTED_QUEUE.
     * Espera receber um JSON com campos: orderId, orderValue e timestamp.
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_REQUESTED_QUEUE)
    public void receiveOrderRequestedEvent(Map<String, Object> message) {
        try {
            String orderId = (String) message.get("orderId");
            BigDecimal orderValue = new BigDecimal(message.get("orderValue").toString());

            LocalDateTime timestamp = LocalDateTime.parse((String) message.get("timestamp"));


            Payment payment = paymentService.createPayment(orderId, orderValue, timestamp);
            System.out.println("Pedido registrado: " + payment.getOrderId());
        } catch (Exception e) {
            System.err.println("Erro ao processar a mensagem: " + e.getMessage());
        }
    }
}
