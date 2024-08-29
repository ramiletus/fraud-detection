package com.ramiletus.frauddetection.service.location.injectlocations;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InjectLocationCommand {

    @NotNull
    private String ip;
    @NotNull
    private String deviceId;
}
