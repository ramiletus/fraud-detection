package com.ramiletus.frauddetection.persistence.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Getter
@Setter
@Node
public class Operator {

    @Id
    private String name;

    public Operator() {
    }

    public Operator(String name) {
        this.name = name;
    }
}
