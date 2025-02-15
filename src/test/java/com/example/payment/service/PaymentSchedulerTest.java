package com.example.payment.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PaymentSchedulerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentScheduler scheduler;

    @Test
    void testCheckExpiredPayments() {

        scheduler.checkExpiredPayments();

        verify(paymentService, times(1)).updateExpiredPayments();
    }
}