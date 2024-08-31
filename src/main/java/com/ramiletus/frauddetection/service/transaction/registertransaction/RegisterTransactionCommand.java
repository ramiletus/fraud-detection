package com.ramiletus.frauddetection.service.transaction.registertransaction;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterTransactionCommand {

    @NotNull
    private String userId;

    @NotNull
    private String deviceId;

    @NotNull
    private String locationId;

    @NotNull
    private Long    timestamp;

}
