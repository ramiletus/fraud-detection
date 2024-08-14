package com.ramiletus.frauddetection.persistence.dao;

import com.ramiletus.frauddetection.persistence.model.PhoneNumber;
import com.ramiletus.frauddetection.persistence.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface UserDao extends Neo4jRepository<User, Long> {

    //some unused but valid examples for queries
    List<User> findByName(String name);
    List<User> findByPhoneNumbersIsContaining(PhoneNumber phoneNumber);
}
