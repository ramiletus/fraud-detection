package com.ramiletus.frauddetection.service.devices.injectdevices;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InjectDeviceCommand {

    @NotNull
    private String operativeSystem;

    @NotNull
    private String browser;

    @NotNull
    private String userId;
}
