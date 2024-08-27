package com.ramiletus.frauddetection.service.users;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PhoneNumberDTO {

    @NotNull
    private Long number;

    @NotNull
    private Boolean isMainNumber;

    @NotNull
    private String operator;
}
