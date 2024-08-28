package com.ramiletus.frauddetection.persistence.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operator operator = (Operator) o;
        return Objects.equals(name, operator.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
