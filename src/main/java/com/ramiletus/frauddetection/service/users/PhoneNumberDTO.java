package com.ramiletus.frauddetection.service.users;

import lombok.Data;

@Data
public class PhoneNumberDTO {

    private Long number;

    private Boolean isMainNumber;

    private String operator;
}
