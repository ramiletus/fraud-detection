package com.ramiletus.frauddetection.service.devices;

import com.ramiletus.frauddetection.FraudDetectionApplication;
import com.ramiletus.frauddetection.persistence.dao.DeviceDao;
import com.ramiletus.frauddetection.persistence.dao.OperatorDao;
import com.ramiletus.frauddetection.persistence.dao.UserDao;
import com.ramiletus.frauddetection.persistence.model.Device;
import com.ramiletus.frauddetection.persistence.model.User;
import com.ramiletus.frauddetection.service.devices.injectdevices.InjectDeviceCommand;
import com.ramiletus.frauddetection.service.users.PhoneNumberDTO;
import com.ramiletus.frauddetection.service.users.UsersCommandHandler;
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
class DevicesCommandHandlerImplTest {

    @Autowired
    UserDao userDao;

    @Autowired
    OperatorDao operatorDao;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    DevicesCommandHandler devicesCommandHandler;

    @Autowired
    UsersCommandHandler usersCommandHandler;

    @Test
    @Transactional
    void handleInjectDevice() throws InstanceNotFoundException {
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
        UUID insertedUserId = usersCommandHandler.handleInjectUser(injectUserCommand).getId();

        InjectDeviceCommand injectDeviceCommand = new InjectDeviceCommand();

        injectDeviceCommand.setBrowser("Random Browser");
        injectDeviceCommand.setOperativeSystem("TempleOS");
        injectDeviceCommand.setUserId(insertedUserId.toString());

        Device createdDevice = devicesCommandHandler.handleInjectDevice(injectDeviceCommand);

        // Then
        Optional<User> foundUser = userDao.findById(insertedUserId);
        assertAll(
                () -> assertTrue(foundUser.isPresent()),
                () -> assertFalse(foundUser.get().getDevices().isEmpty()),
                () -> assertTrue(foundUser.get().getDevices().contains(createdDevice))
        );
    }
}