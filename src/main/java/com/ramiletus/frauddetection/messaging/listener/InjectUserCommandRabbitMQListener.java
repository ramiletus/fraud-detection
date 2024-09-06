package com.ramiletus.frauddetection.messaging.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramiletus.frauddetection.service.users.UsersCommandHandler;
import com.ramiletus.frauddetection.service.users.injectusers.InjectUserCommand;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InjectUserCommandRabbitMQListener {

    private final UsersCommandHandler userCommandHandler;

    @RabbitListener(queues = "${user.injection.source.name}", id = "user-injection-consumer-jms")
    public void handleInjectUserCommand(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        InjectUserCommand command;
        try {
            command = objectMapper.readValue(message, InjectUserCommand.class);
            userCommandHandler.handle(command);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
