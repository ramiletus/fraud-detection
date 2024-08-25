package com.ramiletus.frauddetection.service.users.injectusers;

import com.ramiletus.frauddetection.service.users.PhoneNumberDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class InjectUserCommand {

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    @NotEmpty
    @Valid
    private List<PhoneNumberDTO> phoneNumbers;
}
