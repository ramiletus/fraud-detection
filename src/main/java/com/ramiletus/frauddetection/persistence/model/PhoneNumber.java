package com.ramiletus.frauddetection.persistence.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.UUID;


@Node
public class PhoneNumber {

    @Id
    @GeneratedValue
    private UUID id;

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
