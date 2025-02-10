package com.example.payment.config;

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

    // Configuração para Order Requested
    public static final String ORDER_REQUESTED_EXCHANGE = "order-requested-exchange";
    public static final String ORDER_REQUESTED_QUEUE = "ORDER_REQUESTED_QUEUE";
    public static final String ORDER_REQUESTED_ROUTING_KEY = "order.requested";

    // Configuração para Order Paid
    public static final String ORDER_PAID_EXCHANGE = "order-paid-exchange";
    public static final String ORDER_PAID_QUEUE = "ORDER_PAID_QUEUE";
    public static final String ORDER_PAID_ROUTING_KEY = "order.paid";

    // Exchange para pedidos solicitados
    @Bean
    public TopicExchange orderRequestedExchange() {
        return new TopicExchange(ORDER_REQUESTED_EXCHANGE);
    }

    // Exchange para pedidos pagos
    @Bean
    public TopicExchange orderPaidExchange() {
        return new TopicExchange(ORDER_PAID_EXCHANGE);
    }

    // Declaração da fila Order Requested com durable=true, exclusive=false, autoDelete=false
    @Bean
    public Queue orderRequestedQueue() {
        return new Queue(ORDER_REQUESTED_QUEUE, true, false, false);
    }

    // Declaração da fila Order Paid com durable=true, exclusive=false, autoDelete=false
    @Bean
    public Queue orderPaidQueue() {
        return new Queue(ORDER_PAID_QUEUE, true, false, false);
    }

    // Binding para a fila Order Requested com a routing key "order.requested"
    @Bean
    public Binding bindingOrderRequested(Queue orderRequestedQueue, TopicExchange orderRequestedExchange) {
        return BindingBuilder.bind(orderRequestedQueue)
                .to(orderRequestedExchange)
                .with(ORDER_REQUESTED_ROUTING_KEY);
    }

    // Binding para a fila Order Paid com a routing key "order.paid"
    @Bean
    public Binding bindingOrderPaid(Queue orderPaidQueue, TopicExchange orderPaidExchange) {
        return BindingBuilder.bind(orderPaidQueue)
                .to(orderPaidExchange)
                .with(ORDER_PAID_ROUTING_KEY);
    }

    // Conversor de mensagens para JSON
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate com conversor de mensagens JSON
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
