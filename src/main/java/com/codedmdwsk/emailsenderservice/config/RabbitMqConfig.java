package com.codedmdwsk.emailsenderservice.config;

import org. springframework. amqp. rabbit. connection. ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String EXCHANGE = "email.exchange";
    public static final String QUEUE = "email.send.queue";
    public static final String ROUTING_KEY = "email.send";

    @Bean
    DirectExchange emailExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    Queue emailQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    Binding emailBinding(Queue emailQueue, DirectExchange emailExchange) {
        return BindingBuilder.bind(emailQueue).to(emailExchange).with(ROUTING_KEY);
    }

    @Bean
    Jackson2JsonMessageConverter jacksonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(converter);
        return template;
    }
}
