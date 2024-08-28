package com.ramiletus.frauddetection.persistence.dao;

import com.ramiletus.frauddetection.persistence.model.Location;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface LocationDao extends Neo4jRepository<Location, String> {

}
