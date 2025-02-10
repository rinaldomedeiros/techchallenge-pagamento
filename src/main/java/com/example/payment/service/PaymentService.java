package com.example.payment.service;

import com.example.payment.config.RabbitMQConfig;
import com.example.payment.model.Payment;
import com.example.payment.model.PaymentStatus;
import com.example.payment.repository.PaymentRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;

    public PaymentService(PaymentRepository paymentRepository, RabbitTemplate rabbitTemplate) {
        this.paymentRepository = paymentRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    public Payment createPayment(String orderId, BigDecimal orderValue, LocalDateTime createdAt) {
        Payment payment = new Payment(orderId, PaymentStatus.PENDENTE, orderValue, createdAt);
        return paymentRepository.save(payment);
    }


    public Payment confirmPayment(String orderId) throws Exception {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new Exception("Pedido não encontrado para orderId: " + orderId));

        if (payment.getPaymentStatus() != PaymentStatus.PENDENTE) {
            throw new Exception("Pagamento não pode ser confirmado, status atual: " + payment.getPaymentStatus());
        }

        payment.setPaymentStatus(PaymentStatus.CONFIRMADO);
        payment.setUpdatedAt(LocalDateTime.now());
        Payment updatedPayment = paymentRepository.save(payment);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_PAID_EXCHANGE,
                RabbitMQConfig.ORDER_PAID_ROUTING_KEY,
                updatedPayment
        );

        return updatedPayment;
    }


    public void updateExpiredPayments() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(5);
        List<Payment> expiredPayments = paymentRepository.findByPaymentStatusAndCreatedAtBefore(PaymentStatus.PENDENTE, threshold);

        expiredPayments.forEach(payment -> {
            payment.setPaymentStatus(PaymentStatus.NAO_EFETUADO);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
        });
    }
}
