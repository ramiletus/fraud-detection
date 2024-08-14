package com.ramiletus.frauddetection.persistence.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;


@Data
public class PhoneNumber {

    @Id
    private Long number;

    private Boolean isMainNumber;

    private String operator;
}
