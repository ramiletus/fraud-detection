package com.ramiletus.frauddetection.service.devices;

import com.ramiletus.frauddetection.persistence.dao.DeviceDao;
import com.ramiletus.frauddetection.persistence.dao.UserDao;
import com.ramiletus.frauddetection.persistence.model.Device;
import com.ramiletus.frauddetection.persistence.model.User;
import com.ramiletus.frauddetection.service.devices.injectdevices.InjectDeviceCommand;
import com.ramiletus.frauddetection.service.users.PhoneNumberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DevicesCommandHandlerImpl implements DevicesCommandHandler {

    private final UserDao userDao;
    private final DeviceDao deviceDao;

    @Override
    public Device handleInjectDevice(InjectDeviceCommand command) throws InstanceNotFoundException {
        Optional<User> foundUser = userDao.findById(UUID.fromString(command.getUserId()));
        if (foundUser.isPresent()){

            User user = foundUser.get();

            Device device = new Device();
            device.setBrowser(command.getBrowser());
            device.setOperativeSystem(command.getOperativeSystem());

            user.getDevices().add(deviceDao.save(device));

            Device createdDevice = deviceDao.save(device);
            userDao.save(user);
            return createdDevice;
        } else {
            throw new InstanceNotFoundException("User not found");
        }

    }
}
