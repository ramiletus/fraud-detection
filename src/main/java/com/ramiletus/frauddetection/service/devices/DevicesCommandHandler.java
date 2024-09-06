package com.ramiletus.frauddetection.service.devices;

import com.ramiletus.frauddetection.persistence.model.Device;
import com.ramiletus.frauddetection.service.devices.injectdevices.InjectDeviceCommand;

import javax.management.InstanceNotFoundException;

public interface DevicesCommandHandler {
    Device handle(InjectDeviceCommand command) throws InstanceNotFoundException;
}
