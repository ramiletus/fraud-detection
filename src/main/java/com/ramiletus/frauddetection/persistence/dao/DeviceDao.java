package com.ramiletus.frauddetection.persistence.dao;

import com.ramiletus.frauddetection.persistence.model.Device;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface DeviceDao extends Neo4jRepository<Device, String> {

}
