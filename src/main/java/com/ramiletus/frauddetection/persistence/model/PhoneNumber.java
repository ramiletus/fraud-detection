package com.ramiletus.frauddetection.persistence.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node
public class PhoneNumber {

    @Id
    @GeneratedValue(value = UUIDStringGenerator.class)
    private String id;

    @Getter
    @Setter
    private Long number;

    @Getter
    @Setter
    private Boolean isMainNumber;

    @Getter
    @Setter
    @Relationship(type="HAS_OPERATOR")
    private Operator operator;

    public PhoneNumber() {
    }
}
