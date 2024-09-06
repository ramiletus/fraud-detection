package com.ramiletus.frauddetection.messaging.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramiletus.frauddetection.service.users.UsersCommandHandler;
import com.ramiletus.frauddetection.service.users.injectusers.InjectUserCommand;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InjectUserCommandKafkaListener {

    private final UsersCommandHandler userCommandHandler;

    @KafkaListener(id="user-injection-consumer-kafka", topics = "${user.injection.source.name}", groupId = "fraud-detection")
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

