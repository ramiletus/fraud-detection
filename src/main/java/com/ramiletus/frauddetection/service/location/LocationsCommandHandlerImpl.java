package com.ramiletus.frauddetection.service.location;

import com.ramiletus.frauddetection.persistence.dao.DeviceDao;
import com.ramiletus.frauddetection.persistence.dao.LocationDao;
import com.ramiletus.frauddetection.persistence.model.Device;
import com.ramiletus.frauddetection.persistence.model.Location;
import com.ramiletus.frauddetection.service.location.injectlocations.InjectLocationCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LocationsCommandHandlerImpl implements LocationsCommandHandler {

    private final LocationDao locationDao;
    private final DeviceDao deviceDao;
    private final LocationService locationService;

    @Override
    public Location handleInjectLocation(InjectLocationCommand command) throws InstanceNotFoundException {

        Optional<Device> foundDevice = deviceDao.findById(command.getDeviceId());

        if (!foundDevice.isPresent()) {
            throw new InstanceNotFoundException("Device not found");
        } else {
            Device device = foundDevice.get();
            Location newLocation = LocationMapper.INSTANCE.locationDataToLocation(locationService.getLocationFromIp(command.getIp()));
            device.setLocation(newLocation);

            deviceDao.save(device);

            return locationDao.save(newLocation);
        }
    }
}
