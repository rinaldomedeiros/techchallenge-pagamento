package com.example.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OrderPaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderPaymentApplication.class, args);
    }
}