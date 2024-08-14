package com.ramiletus.frauddetection.service.users.injectusers;

import com.ramiletus.frauddetection.service.users.PhoneNumberDTO;
import lombok.Data;

import java.util.List;

@Data
public class InjectUserCommand {
    private String name;
    private String email;
    private List<PhoneNumberDTO> phoneNumbers;
}
