package com.ramiletus.frauddetection.service.users;

import com.ramiletus.frauddetection.persistence.model.PhoneNumber;

public interface PhoneNumberService {

    PhoneNumber createPhoneNumber(PhoneNumberDTO phoneNumberDTO);
}
