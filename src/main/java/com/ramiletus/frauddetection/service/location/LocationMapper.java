package com.ramiletus.frauddetection.service.location;


import com.ramiletus.frauddetection.persistence.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper( LocationMapper.class );

    Location locationDataToLocation(LocationData locationData);

}
