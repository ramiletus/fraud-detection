package com.ramiletus.frauddetection.messaging.listener;

import com.ramiletus.frauddetection.FraudDetectionApplication;
import com.ramiletus.frauddetection.persistence.dao.UserDao;
import com.ramiletus.frauddetection.persistence.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;

@SpringBootTest(classes = {FraudDetectionApplication.class, RabbitMQTestConfig.class})
public class UserInjectionRabbitMQListenerTest {

    private final String uniqueEmail = UUID.randomUUID().toString().concat("@udc.es");

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserDao userDao;

    @Autowired
    private Queue queue;

    @SpyBean
    private InjectUserCommandRabbitMQListener listener;

    private final CountDownLatch latch = new CountDownLatch(1);

    @AfterEach
    public void cleanUp() {
        List<User> foundUsers = userDao.findByEmail(uniqueEmail);
        if (!foundUsers.isEmpty()) {
            userDao.delete(foundUsers.get(0));
        }
    }

    @Test
    public void test() throws InterruptedException {
        doAnswer(invocation -> {
            try {
                invocation.callRealMethod();
            } finally {
                latch.countDown();
            }
            return null;
        }).when(listener).handleInjectUserCommand(anyString());

        rabbitTemplate.convertAndSend(queue.getName(), "{\n    \"name\": \"Juan Ramil\",\n    \"email\": \"" + uniqueEmail + "\",\n    \"phoneNumbers\": [\n        {\n            \"number\": 626262626,\n            \"isMainNumber\": false,\n            \"operator\": \"Telefonica\"\n        }, \n        {\n            \"number\": 655555555,\n            \"isMainNumber\": true,\n            \"operator\": \"Vodafone\"\n        }\n    ]\n}");

        assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();

        List<User> foundUser = userDao.findByEmail(uniqueEmail);
        assertThat(foundUser).hasSize(1);
        assertThat(foundUser.get(0).getName()).isEqualTo("Juan Ramil");
    }
}
