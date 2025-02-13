package com.example.payment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    public static final String ORDER_REQUESTED_EXCHANGE = "order-requested-exchange";
    public static final String ORDER_REQUESTED_QUEUE = "ORDER_REQUESTED_QUEUE";
    public static final String ORDER_REQUESTED_ROUTING_KEY = "order.requested";

    public static final String ORDER_PAID_EXCHANGE = "order-paid-exchange";
    public static final String ORDER_PAID_QUEUE = "ORDER_PAID_QUEUE";
    public static final String ORDER_PAID_ROUTING_KEY = "order.paid";

    @Bean
    public TopicExchange orderRequestedExchange() {
        return new TopicExchange(ORDER_REQUESTED_EXCHANGE);
    }

    @Bean
    public TopicExchange orderPaidExchange() {
        return new TopicExchange(ORDER_PAID_EXCHANGE);
    }

    @Bean
    public Queue orderRequestedQueue() {
        return new Queue(ORDER_REQUESTED_QUEUE, true, false, false);
    }

    @Bean
    public Queue orderPaidQueue() {
        return new Queue(ORDER_PAID_QUEUE, true, false, false);
    }

    @Bean
    public Binding bindingOrderRequested(Queue orderRequestedQueue, TopicExchange orderRequestedExchange) {
        return BindingBuilder.bind(orderRequestedQueue)
                .to(orderRequestedExchange)
                .with(ORDER_REQUESTED_ROUTING_KEY);
    }


    @Bean
    public Binding bindingOrderPaid(Queue orderPaidQueue, TopicExchange orderPaidExchange) {
        return BindingBuilder.bind(orderPaidQueue)
                .to(orderPaidExchange)
                .with(ORDER_PAID_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setChannelTransacted(true);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
