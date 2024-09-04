package com.ramiletus.frauddetection.messaging.listener;

import com.ramiletus.frauddetection.persistence.dao.UserDao;
import com.ramiletus.frauddetection.persistence.model.User;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AcknowledgingConsumerAwareMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class UserInjectionKafkaListenerTest {

    // ignore error "Could not autowire. No beans of 'KafkaListenerEndpointRegistry' type found."
    // turns out it's a bug in the linter
    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @Autowired
    private InjectUserCommandKafkaListener injectUserCommandKafkaListener;

    @Autowired
    private KafkaTemplate<String, String> template;

    @Autowired
    private UserDao userDao;


    @AfterEach
    public void tearDown() {
        /* TODO: there has to be a better way to do this, but I was not able to use @DirtiesContext tag
            properly to reset the database between tests in this class (the created user is present in db after tests)
        */
        userDao.delete(userDao.findByEmail("uniquejuanram2@udc.es").get(0));
    }

    @Test
    public void test() throws Exception {
        ConcurrentMessageListenerContainer<?, ?> container = (ConcurrentMessageListenerContainer<?, ?>) registry
                .getListenerContainer("user-injection-consumer");
        container.stop();

        @SuppressWarnings("unchecked")
        AcknowledgingConsumerAwareMessageListener<String, String> messageListener = (AcknowledgingConsumerAwareMessageListener<String, String>) container
                .getContainerProperties().getMessageListener();

        CountDownLatch latch = new CountDownLatch(1);

        container.getContainerProperties()
                .setMessageListener(new AcknowledgingConsumerAwareMessageListener<String, String>() {

                    @Override
                    public void onMessage(ConsumerRecord<String, String> data, Acknowledgment acknowledgment,
                                          Consumer<?, ?> consumer) {
                        messageListener.onMessage(data, acknowledgment, consumer);
                        latch.countDown();
                    }

                });
        container.start();
        template.send("user-injection", "{\n    \"name\": \"Juan Ramil\",\n    \"email\": \"uniquejuanram2@udc.es\",\n    \"phoneNumbers\": [\n        {\n            \"number\": 626262626,\n            \"isMainNumber\": false,\n            \"operator\": \"Telefonica\"\n        }, \n        {\n            \"number\": 655555555,\n            \"isMainNumber\": true,\n            \"operator\": \"Vodafone\"\n        }\n    ]\n}");


        assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
        List<User> foundUser = userDao.findByEmail("uniquejuanram2@udc.es");
        assertThat(foundUser).hasSize(1);
        assertThat(foundUser.get(0).getName()).isEqualTo("Juan Ramil");
    }
}
