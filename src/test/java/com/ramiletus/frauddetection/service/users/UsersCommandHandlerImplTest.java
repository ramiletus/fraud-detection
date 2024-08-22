package com.ramiletus.frauddetection.service.users;

import com.ramiletus.frauddetection.FraudDetectionApplication;
import com.ramiletus.frauddetection.persistence.dao.UserDao;
import com.ramiletus.frauddetection.persistence.model.User;
import com.ramiletus.frauddetection.service.users.injectusers.InjectUserCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FraudDetectionApplication.class)
@Transactional
class UsersCommandHandlerTest {

    @Autowired
    UserDao userDao;

    @Autowired
    UsersCommandHandler usersCommandHandler;

    @Test
    void handleInjectUser() {
        // Given
        InjectUserCommand injectUserCommand = new InjectUserCommand();
        injectUserCommand.setName("John Doe");
        injectUserCommand.setEmail("johndoe@udc.es");

        PhoneNumberDTO phoneNumber1 = new PhoneNumberDTO();

        phoneNumber1.setNumber(123456789L);
        phoneNumber1.setIsMainNumber(true);
        phoneNumber1.setOperator("Operator");

        injectUserCommand.setPhoneNumbers(List.of(phoneNumber1));

        // When
        usersCommandHandler.handleInjectUser(injectUserCommand);

        // Then
        List<User> foundUsers = userDao.findByEmail("johndoe@udc.es");
        assertNotNull(foundUsers);
        assertNotNull(foundUsers.get(0));
        assertEquals(1, foundUsers.size());
        assertEquals("John Doe", foundUsers.get(0).getName());

    }

}