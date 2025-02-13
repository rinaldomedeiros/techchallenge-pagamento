package com.example.payment.consumer;

import com.example.payment.config.RabbitMQConfig;
import com.example.payment.dto.OrderRequestedMessageDTO;
import com.example.payment.model.Payment;
import com.example.payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentConsumer {

    private final PaymentService paymentService;
    private static final Logger logger = LoggerFactory.getLogger(PaymentConsumer.class);

    public PaymentConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Método que consome mensagens da fila ORDER_REQUESTED_QUEUE.
     * Espera receber um JSON com os campos: orderId, amount, timestamp e (opcionalmente) status.
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_REQUESTED_QUEUE)
    public void receiveOrderRequestedEvent(OrderRequestedMessageDTO messageDTO) {
        try {
            messageDTO.setTimestamp(LocalDateTime.now());
            messageDTO.setPaymentStatus("PENDENTE");

            Payment payment = paymentService.createPayment(
                    messageDTO.getOrderId(),
                    messageDTO.getOrderValue(),
                    messageDTO.getTimestamp()
            );

            logger.info("Pedido registrado: " + payment.getOrderId());
        } catch (Exception e) {
            logger.error("Erro ao processar a mensagem: " + e.getMessage());
        }
    }
}