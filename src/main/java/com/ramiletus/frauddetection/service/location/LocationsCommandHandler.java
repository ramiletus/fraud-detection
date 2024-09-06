package com.ramiletus.frauddetection.service.location;

import com.ramiletus.frauddetection.persistence.model.Location;
import com.ramiletus.frauddetection.service.location.injectlocations.InjectLocationCommand;

import javax.management.InstanceNotFoundException;

public interface LocationsCommandHandler {
    Location handle(InjectLocationCommand command) throws InstanceNotFoundException;
}
