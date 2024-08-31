package com.ramiletus.frauddetection.persistence.dao;

import com.ramiletus.frauddetection.persistence.model.Transaction;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;


public interface TransactionDao extends Neo4jRepository<Transaction, String> {

    @Query("""
            MATCH (u:User)<-[:MADE_BY]-(t:Transaction)-[:MADE_FROM]->(l:Location)
                WHERE u.id = $userId
                WITH t, l
                ORDER BY t.timestamp DESC
                LIMIT 1
                WITH l AS lastLocation, t AS lastTransaction, $currentTimestamp AS newTimestamp
                WITH point.distance(
                        point({longitude: lastLocation.lon, latitude: lastLocation.lat}),
                        point({longitude: $newLongitude, latitude: $newLatitude})
                     ) AS distanceTraveled,
                     abs(newTimestamp - lastTransaction.timestamp) AS timeElapsed
                WITH distanceTraveled, timeElapsed,
                     distanceTraveled / (timeElapsed / 1000.0) AS speed
                WHERE speed > $maxSpeed
                RETURN true AS isFraud
        """)
    Boolean detectFraud(
            String userId,
            Double newLongitude,
            Double newLatitude,
            Long currentTimestamp,
            Double maxSpeed
    );
}
