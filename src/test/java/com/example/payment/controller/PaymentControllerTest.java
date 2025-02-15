package com.example.payment.controller;

import com.example.payment.dto.PaymentDTO;
import com.example.payment.model.Payment;
import com.example.payment.model.PaymentStatus;
import com.example.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PaymentController(paymentService)).build();
    }

    @Test
    void testConfirmPaymentSuccess() throws Exception {
        // Mocking the PaymentService behavior
        Payment payment = new Payment();
        payment.setOrderId("123");
        payment.setPaymentStatus(PaymentStatus.CONFIRMADO);
        payment.setOrderValue(new BigDecimal("100.00"));
        payment.setCreatedAt(LocalDateTime.now());

        when(paymentService.confirmPayment(eq("123"))).thenReturn(payment);

        mockMvc.perform(post("/payments/123/confirm")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("123"))
                .andExpect(jsonPath("$.paymentStatus").value("CONFIRMADO"));
    }

    @Test
    void testConfirmPaymentBadRequest() throws Exception {
        // Mocking the PaymentService behavior to throw an exception
        when(paymentService.confirmPayment(eq("123"))).thenThrow(new Exception("Pagamento não encontrado"));

        mockMvc.perform(post("/payments/123/confirm")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetPaymentsByStatusSuccess() throws Exception {
        Payment payment1 = new Payment();
        payment1.setOrderId("123");
        payment1.setPaymentStatus(PaymentStatus.CONFIRMADO);
        payment1.setOrderValue(new BigDecimal("100.00"));
        payment1.setCreatedAt(LocalDateTime.now());

        Payment payment2 = new Payment();
        payment2.setOrderId("456");
        payment2.setPaymentStatus(PaymentStatus.CONFIRMADO);
        payment2.setOrderValue(new BigDecimal("150.00"));
        payment2.setCreatedAt(LocalDateTime.now());

        List<Payment> payments = Arrays.asList(payment1, payment2);

        when(paymentService.getPaymentsByStatus(eq(PaymentStatus.CONFIRMADO))).thenReturn(payments);

        mockMvc.perform(get("/payments?status=CONFIRMADO")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].orderId").value("123"))
                .andExpect(jsonPath("$[1].orderId").value("456"));
    }

    @Test
    void testGetAllPaymentsSuccess() throws Exception {
        Payment payment1 = new Payment();
        payment1.setOrderId("123");
        payment1.setPaymentStatus(PaymentStatus.CONFIRMADO);
        payment1.setOrderValue(new BigDecimal("100.00"));
        payment1.setCreatedAt(LocalDateTime.now());

        Payment payment2 = new Payment();
        payment2.setOrderId("456");
        payment2.setPaymentStatus(PaymentStatus.PENDENTE);
        payment2.setOrderValue(new BigDecimal("150.00"));
        payment2.setCreatedAt(LocalDateTime.now());

        List<Payment> payments = Arrays.asList(payment1, payment2);

        when(paymentService.getAllPayments()).thenReturn(payments);

        mockMvc.perform(get("/payments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].orderId").value("123"))
                .andExpect(jsonPath("$[1].orderId").value("456"));
    }

    @Test
    void testGetPaymentsByInvalidStatus() throws Exception {
        mockMvc.perform(get("/payments?status=INVALID")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
