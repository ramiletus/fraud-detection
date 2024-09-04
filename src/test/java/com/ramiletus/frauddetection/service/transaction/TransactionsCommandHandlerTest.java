package com.ramiletus.frauddetection.service.transaction;

import com.ramiletus.frauddetection.FraudDetectionApplication;
import com.ramiletus.frauddetection.persistence.dao.LocationDao;
import com.ramiletus.frauddetection.persistence.dao.TransactionDao;
import com.ramiletus.frauddetection.persistence.model.Transaction;
import com.ramiletus.frauddetection.service.devices.DevicesCommandHandler;
import com.ramiletus.frauddetection.service.devices.injectdevices.InjectDeviceCommand;
import com.ramiletus.frauddetection.service.location.LocationsCommandHandler;
import com.ramiletus.frauddetection.service.location.injectlocations.InjectLocationCommand;
import com.ramiletus.frauddetection.service.transaction.registertransaction.RegisterTransactionCommand;
import com.ramiletus.frauddetection.service.users.PhoneNumberDTO;
import com.ramiletus.frauddetection.service.users.UsersCommandHandlerImpl;
import com.ramiletus.frauddetection.service.users.injectusers.InjectUserCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FraudDetectionApplication.class)
class TransactionsCommandHandlerTest {

    @Autowired
    LocationDao locationDao;

    @Autowired
    LocationsCommandHandler locationsCommandHandler;

    @Autowired
    UsersCommandHandlerImpl usersCommandHandler;

    @Autowired
    DevicesCommandHandler devicesCommandHandler;

    @Autowired
    private TransactionCommandHandler transactionCommandHandler;

    @Autowired
    private TransactionDao transactionDao;

    @Test
    @Transactional
    void handleRegisterTransaction() throws InstanceNotFoundException {

        // Given
        InjectUserCommand injectUserCommand = new InjectUserCommand();
        injectUserCommand.setName("John Doe");
        injectUserCommand.setEmail("johndoe@udc.es");

        PhoneNumberDTO phoneNumber1 = new PhoneNumberDTO();

        phoneNumber1.setNumber(123456789L);
        phoneNumber1.setIsMainNumber(true);
        phoneNumber1.setOperator("Operator");

        injectUserCommand.setPhoneNumbers(List.of(phoneNumber1));

        String insertedUserId = usersCommandHandler.handle(injectUserCommand).getId();

        InjectDeviceCommand injectDeviceCommand = new InjectDeviceCommand();

        injectDeviceCommand.setBrowser("Random Browser");
        injectDeviceCommand.setOperativeSystem("TempleOS");
        injectDeviceCommand.setUserId(insertedUserId);

        String insertedDeviceId = devicesCommandHandler.handle(injectDeviceCommand).getId();

        InjectLocationCommand injectLocationCommand1 = new InjectLocationCommand();
        injectLocationCommand1.setIp("24.48.0.1");
        injectLocationCommand1.setDeviceId(insertedDeviceId);

        String insetedLocation1Id = locationsCommandHandler.handle(injectLocationCommand1).getId();

        InjectLocationCommand injectLocationCommand2 = new InjectLocationCommand();
        injectLocationCommand2.setIp("24.155.246.135");
        injectLocationCommand2.setDeviceId(insertedDeviceId);

        String insetedLocation2Id = locationsCommandHandler.handle(injectLocationCommand2).getId();


        // When
        RegisterTransactionCommand registerTransactionCommand1 = new RegisterTransactionCommand();
        registerTransactionCommand1.setUserId(insertedUserId);
        registerTransactionCommand1.setDeviceId(insertedDeviceId);
        registerTransactionCommand1.setLocationId(insetedLocation1Id);
        registerTransactionCommand1.setTimestamp(System.currentTimeMillis()-10000);

        RegisterTransactionCommand registerTransactionCommand2 = new RegisterTransactionCommand();
        registerTransactionCommand2.setUserId(insertedUserId);
        registerTransactionCommand2.setDeviceId(insertedDeviceId);
        registerTransactionCommand2.setLocationId(insetedLocation2Id);
        registerTransactionCommand2.setTimestamp(System.currentTimeMillis());

        String transaction1Id = transactionCommandHandler.handle(registerTransactionCommand1).getId();
        String transaction2Id = transactionCommandHandler.handle(registerTransactionCommand2).getId();

        // Then
        Optional<Transaction> foundTransaction1 = transactionDao.findById(transaction1Id);
        Optional<Transaction> foundTransaction2 = transactionDao.findById(transaction2Id);
        assertAll(
                () -> assertTrue(foundTransaction1.isPresent()),
                () -> assertTrue(foundTransaction2.isPresent()),
                () -> assertFalse(foundTransaction1.get().getIsFraud()),
                () -> assertTrue(foundTransaction2.get().getIsFraud())
        );
    }

    @Test
    @Transactional
    void handleRegisterTransactionToInValidIds(){

        // Given
        RegisterTransactionCommand registerTransactionCommand1 = new RegisterTransactionCommand();
        registerTransactionCommand1.setUserId(UUID.randomUUID().toString());
        registerTransactionCommand1.setDeviceId(UUID.randomUUID().toString());
        registerTransactionCommand1.setLocationId(UUID.randomUUID().toString());
        registerTransactionCommand1.setTimestamp(System.currentTimeMillis()-10000);

        // Then
        assertThrows(InstanceNotFoundException.class, () -> transactionCommandHandler.handle(registerTransactionCommand1));
    }

}