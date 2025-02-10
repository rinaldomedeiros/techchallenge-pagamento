package com.example.payment.producer;

import com.example.payment.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentProducer {

    private final RabbitTemplate rabbitTemplate;

    public PaymentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Envia um evento simulando um pedido para o exchange de pedidos solicitados.
     */
    public void sendOrderRequestedEvent(String orderId, BigDecimal orderValue) {
        Map<String, Object> message = new HashMap<>();
        message.put("orderId", orderId);
        message.put("orderValue", orderValue);
        message.put("timestamp", LocalDateTime.now().toString());
        message.put("paymentStatus", "PENDENTE");

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_REQUESTED_EXCHANGE,
                RabbitMQConfig.ORDER_REQUESTED_ROUTING_KEY,
                message
        );
        System.out.println("Evento de pedido enviado: " + orderId);
    }
}
