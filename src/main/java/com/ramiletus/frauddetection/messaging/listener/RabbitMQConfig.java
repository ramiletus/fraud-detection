package com.ramiletus.frauddetection.messaging.listener;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("rabbitmq")
public class RabbitMQConfig {

    @Bean
    public Queue userInjectionQueue(@Value("${user.injection.source.name}") String userInjectionQueue) {
        return new Queue(userInjectionQueue, true, false, false);
    }

    @Bean
    public Exchange exchange() {
        return new DirectExchange("user-injection-exchange");
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with("routing-key")
                .noargs();
    }
}