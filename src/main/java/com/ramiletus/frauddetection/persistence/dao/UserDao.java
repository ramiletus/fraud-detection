package com.ramiletus.frauddetection.persistence.dao;

import com.ramiletus.frauddetection.persistence.model.PhoneNumber;
import com.ramiletus.frauddetection.persistence.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;
import java.util.UUID;

public interface UserDao extends Neo4jRepository<User, UUID> {

    //some unused but valid examples for queries
    List<User> findByName(String name);
    List<User> findByEmail(String email);
    List<User> findByPhoneNumbersIsContaining(PhoneNumber phoneNumber);
}
