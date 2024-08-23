package com.ramiletus.frauddetection.persistence.dao;

import com.ramiletus.frauddetection.persistence.model.Operator;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;


public interface OperatorDao extends Neo4jRepository<Operator, String> {

    Optional<Operator> findByName(String name);
}
