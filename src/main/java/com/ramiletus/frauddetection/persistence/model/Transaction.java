package com.ramiletus.frauddetection.persistence.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Getter
@Node
public class Transaction {

    @Id
    @GeneratedValue(value = UUIDStringGenerator.class)
    private String id;

    @Setter
    @Relationship(type = "MADE_BY")
    private User user;

    @Setter
    @Relationship(type = "MADE_FROM")
    private Location location;

    @Setter
    @Relationship(type = "MADE_WITH")
    private Device device;

    @Setter
    private Long timestamp;

    @Setter
    private int isFraud;
}
