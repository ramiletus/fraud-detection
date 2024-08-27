package com.ramiletus.frauddetection.service.users;


import com.ramiletus.frauddetection.persistence.model.PhoneNumber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PhoneNumberMapper {

    PhoneNumberMapper INSTANCE = Mappers.getMapper( PhoneNumberMapper.class );

    @Mapping(target = "operator", ignore = true)
    PhoneNumber phoneNumberDTOToPhoneNumber(PhoneNumberDTO phoneNumberDTO);

}
