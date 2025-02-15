package com.example.payment.service;

import com.example.payment.dto.OrderRequestedMessageDTO;
import com.example.payment.model.Payment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentConsumerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentConsumer consumer;

    @Test
    void testReceiveOrderRequestedEvent_Success() {

        OrderRequestedMessageDTO dto = new OrderRequestedMessageDTO();
        dto.setOrderId("order-123");
        dto.setOrderValue(BigDecimal.valueOf(150.0));

        Payment payment = new Payment();
        payment.setOrderId(dto.getOrderId());
        when(paymentService.createPayment(
                eq(dto.getOrderId()),
                eq(dto.getOrderValue()),
                any(LocalDateTime.class)))
                .thenReturn(payment);

        consumer.receiveOrderRequestedEvent(dto);

        assertNotNull(dto.getTimestamp());
        assertEquals("PENDENTE", dto.getPaymentStatus());

        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(paymentService).createPayment(eq(dto.getOrderId()), eq(dto.getOrderValue()), captor.capture());
        assertEquals(dto.getTimestamp(), captor.getValue());
    }

    @Test
    void testReceiveOrderRequestedEvent_Exception() {

        OrderRequestedMessageDTO dto = new OrderRequestedMessageDTO();
        dto.setOrderId("order-456");
        dto.setOrderValue(BigDecimal.valueOf(300.0));

        when(paymentService.createPayment(anyString(), BigDecimal.valueOf(anyDouble()), any(LocalDateTime.class)))
                .thenThrow(new RuntimeException("Erro de teste"));

        assertDoesNotThrow(() -> consumer.receiveOrderRequestedEvent(dto));

        verify(paymentService).createPayment(eq(dto.getOrderId()), eq(dto.getOrderValue()), any(LocalDateTime.class));
    }
}
