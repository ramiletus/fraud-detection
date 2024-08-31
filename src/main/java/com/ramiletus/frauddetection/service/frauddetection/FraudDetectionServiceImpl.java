package com.ramiletus.frauddetection.service.frauddetection;

import com.ramiletus.frauddetection.persistence.dao.TransactionDao;
import com.ramiletus.frauddetection.persistence.model.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FraudDetectionServiceImpl implements FraudDetectionService {

    private final TransactionDao transactionDao;

    @Override
    public boolean evaluateFraud(Transaction transaction) {

        String userId = transaction.getUser().getId();
        Double newLongitude = transaction.getLocation().getLon();
        Double newLatitude = transaction.getLocation().getLat();
        Long currentTimestamp = transaction .getTimestamp();
        Double maxSpeed = 150.0;

        return Boolean.TRUE.equals(transactionDao.detectFraud(
                userId,
                newLongitude,
                newLatitude,
                currentTimestamp,
                maxSpeed
        ));
    }
}
