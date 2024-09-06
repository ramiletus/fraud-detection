package com.ramiletus.frauddetection.controller.command;

import com.ramiletus.frauddetection.service.devices.DevicesCommandHandler;
import com.ramiletus.frauddetection.service.devices.injectdevices.InjectDeviceCommand;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.InstanceNotFoundException;

@RestController
@RequestMapping("/devices")
public class DevicesController {

    private final DevicesCommandHandler devicesCommandHandler;

    public DevicesController(DevicesCommandHandler devicesCommandHandler) {
        this.devicesCommandHandler = devicesCommandHandler;
    }

    @PostMapping(value = "/inject")
    public void injectUsers(@Valid @RequestBody InjectDeviceCommand injectDeviceCommand) throws InstanceNotFoundException {
        devicesCommandHandler.handle(injectDeviceCommand);
    }

}
