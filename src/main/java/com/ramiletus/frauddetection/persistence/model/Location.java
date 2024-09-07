package com.ramiletus.frauddetection.persistence.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Data
@Getter
@Node
public class Location {

    @Id
    @GeneratedValue(value = UUIDStringGenerator.class)
    private String id;

    @Setter
    public String country;

    @Setter
    public String ip;

    @Setter
    private String region;

    @Setter
    public String city;

    @Setter
    public Double lat;

    @Setter
    public Double lon;
}
