package com.example.payment.service;

import com.example.payment.config.RabbitMQConfig;
import com.example.payment.dto.OrderPaidMessageDTO;
import com.example.payment.model.Payment;
import com.example.payment.model.PaymentStatus;
import com.example.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePayment() {
        Payment payment = new Payment();
        payment.setOrderId("123");
        payment.setPaymentStatus(PaymentStatus.PENDENTE);
        payment.setOrderValue(new BigDecimal("100.00"));
        payment.setCreatedAt(LocalDateTime.now());

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.createPayment("123", new BigDecimal("100.00"), LocalDateTime.now());

        assertNotNull(result);
        assertEquals("123", result.getOrderId());
        assertEquals(PaymentStatus.PENDENTE, result.getPaymentStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testConfirmPayment() throws Exception {
        Payment payment = new Payment();
        payment.setOrderId("123");
        payment.setPaymentStatus(PaymentStatus.PENDENTE);
        payment.setOrderValue(new BigDecimal("100.00"));
        payment.setCreatedAt(LocalDateTime.now());

        when(paymentRepository.findByOrderId("123")).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.confirmPayment("123");

        assertEquals(PaymentStatus.CONFIRMADO, result.getPaymentStatus());
        verify(paymentRepository, times(1)).findByOrderId("123");
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.ORDER_PAID_EXCHANGE),
                eq(RabbitMQConfig.ORDER_PAID_ROUTING_KEY),
                any(OrderPaidMessageDTO.class)
        );
    }

    @Test
    void testConfirmPaymentNotFound() {
        when(paymentRepository.findByOrderId("123")).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            paymentService.confirmPayment("123");
        });

        assertEquals("Pedido não encontrado para orderId: 123", exception.getMessage());
        verify(paymentRepository, times(1)).findByOrderId("123");
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testConfirmPaymentInvalidStatus() {
        Payment payment = new Payment();
        payment.setOrderId("123");
        payment.setPaymentStatus(PaymentStatus.CONFIRMADO); // Status já confirmado

        when(paymentRepository.findByOrderId("123")).thenReturn(Optional.of(payment));

        Exception exception = assertThrows(Exception.class, () -> {
            paymentService.confirmPayment("123");
        });

        assertEquals("Pagamento não pode ser confirmado, status atual: CONFIRMADO", exception.getMessage());
        verify(paymentRepository, times(1)).findByOrderId("123");
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = List.of(new Payment(), new Payment());
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(2, result.size());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void testUpdateExpiredPayments() {
        Payment payment = new Payment();
        payment.setOrderId("123");
        payment.setPaymentStatus(PaymentStatus.PENDENTE);
        payment.setCreatedAt(LocalDateTime.now().minusMinutes(10)); // Pagamento expirado

        when(paymentRepository.findByPaymentStatusAndCreatedAtBefore(eq(PaymentStatus.PENDENTE), any(LocalDateTime.class)))
                .thenReturn(List.of(payment));

        paymentService.updateExpiredPayments();

        assertEquals(PaymentStatus.NAO_EFETUADO, payment.getPaymentStatus());
        verify(paymentRepository, times(1)).save(payment);
    }
}

