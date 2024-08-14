package com.ramiletus.frauddetection.service.users;


import com.ramiletus.frauddetection.persistence.model.PhoneNumber;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PhoneNumberMapper {

    PhoneNumberMapper INSTANCE = Mappers.getMapper( PhoneNumberMapper.class );

    PhoneNumber phoneNumberDTOToPhoneNumber(PhoneNumberDTO phoneNumberDTO);

    List<PhoneNumber> phoneNumberDTOListToPhoneNUmberList(List<PhoneNumberDTO> phoneNumberDTOList);
}
