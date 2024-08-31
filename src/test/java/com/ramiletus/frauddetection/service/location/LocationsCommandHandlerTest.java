package com.ramiletus.frauddetection.service.location;

import com.ramiletus.frauddetection.FraudDetectionApplication;
import com.ramiletus.frauddetection.persistence.dao.LocationDao;
import com.ramiletus.frauddetection.persistence.dao.UserDao;
import com.ramiletus.frauddetection.persistence.model.Device;
import com.ramiletus.frauddetection.persistence.model.User;
import com.ramiletus.frauddetection.service.devices.DevicesCommandHandler;
import com.ramiletus.frauddetection.service.devices.injectdevices.InjectDeviceCommand;
import com.ramiletus.frauddetection.service.location.injectlocations.InjectLocationCommand;
import com.ramiletus.frauddetection.service.transaction.TransactionCommandHandler;
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
class LocationsCommandHandlerTest {

    @Autowired
    LocationDao locationDao;

    @Autowired
    LocationsCommandHandler locationsCommandHandler;

    @Autowired
    UsersCommandHandlerImpl usersCommandHandler;

    @Autowired
    DevicesCommandHandler devicesCommandHandler;

    @Autowired
    TransactionCommandHandler transactionCommandHandler;

    @Autowired
    private UserDao userDao;

    @Test
    @Transactional
    void handleInjectLocation() throws InstanceNotFoundException {
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
        String insertedUserId = usersCommandHandler.handleInjectUser(injectUserCommand).getId();

        InjectDeviceCommand injectDeviceCommand = new InjectDeviceCommand();

        injectDeviceCommand.setBrowser("Random Browser");
        injectDeviceCommand.setOperativeSystem("TempleOS");
        injectDeviceCommand.setUserId(insertedUserId);

        Device createdDevice = devicesCommandHandler.handleInjectDevice(injectDeviceCommand);

        String insertedDeviceId = createdDevice.getId();

        InjectLocationCommand injectLocationCommand = new InjectLocationCommand();
        injectLocationCommand.setIp("24.48.0.1");
        injectLocationCommand.setDeviceId(insertedDeviceId);

        // When

        String insetedLocationId = locationsCommandHandler.handleInjectLocation(injectLocationCommand).getId();

        // Then
        Optional<User> foundUser = userDao.findById(insertedUserId);
        assertAll(
                () -> assertTrue(foundUser.isPresent()),
                () -> assertFalse(foundUser.get().getDevices().stream().findFirst().isEmpty()),
                () -> assertEquals(foundUser.get().getDevices().stream().findFirst().get().getLocation().getId(), insetedLocationId)
        );
    }

    @Test
    @Transactional
    void handleInjectLocationToInValidDeviceId(){
        // Given

        RegisterTransactionCommand registerTransactionCommand = new RegisterTransactionCommand();
        registerTransactionCommand.setUserId(UUID.randomUUID().toString());
        registerTransactionCommand.setDeviceId(UUID.randomUUID().toString());
        registerTransactionCommand.setLocationId(UUID.randomUUID().toString());
        registerTransactionCommand.setTimestamp(System.currentTimeMillis());

        // Then
        assertThrows(InstanceNotFoundException.class, () ->transactionCommandHandler.handleTransaction(registerTransactionCommand));
    }

}