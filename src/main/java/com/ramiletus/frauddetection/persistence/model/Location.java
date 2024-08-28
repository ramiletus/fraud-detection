package com.ramiletus.frauddetection.persistence.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Data
@Getter
@Setter
public class Location {

    @Id
    @GeneratedValue(value = UUIDStringGenerator.class)
    private String id;

    public String country;

    private String region;

    public String city;

    public Double lat;

    public Double lon;
}
