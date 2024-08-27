package com.ramiletus.frauddetection.service.users;

import com.ramiletus.frauddetection.FraudDetectionApplication;
import com.ramiletus.frauddetection.persistence.dao.OperatorDao;
import com.ramiletus.frauddetection.persistence.dao.UserDao;
import com.ramiletus.frauddetection.persistence.model.Operator;
import com.ramiletus.frauddetection.persistence.model.User;
import com.ramiletus.frauddetection.service.users.injectusers.InjectUserCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FraudDetectionApplication.class)
class UsersCommandHandlerTest {

    @Autowired
    UserDao userDao;

    @Autowired
    OperatorDao operatorDao;

    @Autowired
    UsersCommandHandler usersCommandHandler;

    @Test
    @Transactional
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


    @Test
    @Transactional
    void onlyOneOperatorByNameIsCreated() {
        // Given
        InjectUserCommand injectUserCommand1 = new InjectUserCommand();
        injectUserCommand1.setName("John Doe");
        injectUserCommand1.setEmail("johndoe@udc.es");

        PhoneNumberDTO phoneNumber1 = new PhoneNumberDTO();

        phoneNumber1.setNumber(123456789L);
        phoneNumber1.setIsMainNumber(true);
        phoneNumber1.setOperator("Operator1");

        PhoneNumberDTO phoneNumber2 = new PhoneNumberDTO();

        phoneNumber1.setNumber(987654321L);
        phoneNumber1.setIsMainNumber(false);
        phoneNumber1.setOperator("Operator2");

        injectUserCommand1.setPhoneNumbers(List.of(phoneNumber1, phoneNumber2));


        InjectUserCommand injectUserCommand2 = new InjectUserCommand();
        injectUserCommand2.setName("Don Joe");
        injectUserCommand2.setEmail("donjoe@udc.es");


        PhoneNumberDTO phoneNumber3 = new PhoneNumberDTO();
        phoneNumber3.setNumber(666666666L);
        phoneNumber3.setIsMainNumber(true);
        phoneNumber3.setOperator("Operator1");

        injectUserCommand2.setPhoneNumbers(List.of(phoneNumber3));

        // When
        usersCommandHandler.handleInjectUser(injectUserCommand1);
        usersCommandHandler.handleInjectUser(injectUserCommand2);

        // Then
        Optional<Operator> foundOperator = operatorDao.findByName("Operator1");

        assertAll(
                () -> assertTrue(foundOperator.isPresent()),
                () -> assertEquals("Operator1", foundOperator.get().getName())
        );
    }

}