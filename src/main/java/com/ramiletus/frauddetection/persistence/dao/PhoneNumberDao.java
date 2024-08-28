package com.ramiletus.frauddetection.persistence.dao;

import com.ramiletus.frauddetection.persistence.model.PhoneNumber;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PhoneNumberDao extends Neo4jRepository<PhoneNumber, String> {

}
