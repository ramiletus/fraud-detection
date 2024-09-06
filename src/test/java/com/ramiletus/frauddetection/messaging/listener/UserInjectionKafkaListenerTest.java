package com.ramiletus.frauddetection.messaging.listener;

import com.ramiletus.frauddetection.persistence.dao.UserDao;
import com.ramiletus.frauddetection.persistence.model.User;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AcknowledgingConsumerAwareMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class UserInjectionKafkaListenerTest {

    @Value("${user.injection.source.name}")
    private String queueName;

    private final String uniqueEmail = UUID.randomUUID().toString().concat("@udc.es");

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
        userDao.delete(userDao.findByEmail(uniqueEmail).get(0));
    }

    @Test
    public void test() throws Exception {
        ConcurrentMessageListenerContainer<?, ?> container = (ConcurrentMessageListenerContainer<?, ?>) registry
                .getListenerContainer("user-injection-consumer-kafka");
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
        template.send(this.queueName, "{\n    \"name\": \"Juan Ramil\",\n    \"email\": \"" + uniqueEmail + "\",\n    \"phoneNumbers\": [\n        {\n            \"number\": 626262626,\n            \"isMainNumber\": false,\n            \"operator\": \"Telefonica\"\n        }, \n        {\n            \"number\": 655555555,\n            \"isMainNumber\": true,\n            \"operator\": \"Vodafone\"\n        }\n    ]\n}");

        /*
            TODO: this first assertion [latch.await()] just fails sometimes, a revision on why is pending.
                Seems like the await time passes without the latch being counted down.
                Happens a few times only, but this makes the test not reliable.
                With the actual configuration, not too much info is provided in test logs.
        */
        assertThat(latch.await(60, TimeUnit.SECONDS)).isTrue();
        List<User> foundUser = userDao.findByEmail(uniqueEmail);
        assertThat(foundUser).hasSize(1);
        assertThat(foundUser.get(0).getName()).isEqualTo("Juan Ramil");
    }
}
