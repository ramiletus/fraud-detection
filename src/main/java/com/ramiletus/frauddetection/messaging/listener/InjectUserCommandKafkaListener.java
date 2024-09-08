package com.ramiletus.frauddetection.messaging.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramiletus.frauddetection.service.users.UsersCommandHandler;
import com.ramiletus.frauddetection.service.users.injectusers.InjectUserCommand;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Profile("kafka")
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

    @Bean
    public KafkaAdmin admin(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(AdminClientConfig.RETRIES_CONFIG, 10);
        return new KafkaAdmin(configs);
    }

    @Bean
    public KafkaAdmin.NewTopics topics(@Value("${user.injection.source.name}") String userInjectionTopic) {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(userInjectionTopic)
                        .build());
    }
}

