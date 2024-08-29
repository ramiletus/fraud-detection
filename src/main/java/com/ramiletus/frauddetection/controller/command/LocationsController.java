package com.ramiletus.frauddetection.controller.command;

import com.ramiletus.frauddetection.service.location.LocationsCommandHandler;
import com.ramiletus.frauddetection.service.location.injectlocations.InjectLocationCommand;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.InstanceNotFoundException;

@RestController
@RequestMapping("/locations")
public class LocationsController {

    private final LocationsCommandHandler locationsCommandHandler;

    public LocationsController(LocationsCommandHandler locationsCommandHandler) {
        this.locationsCommandHandler = locationsCommandHandler;
    }

    @PostMapping(value = "/inject")
    public void injectUsers(@Valid @RequestBody InjectLocationCommand injectLocationCommand) throws InstanceNotFoundException {
        locationsCommandHandler.handleInjectLocation(injectLocationCommand);
    }

}
