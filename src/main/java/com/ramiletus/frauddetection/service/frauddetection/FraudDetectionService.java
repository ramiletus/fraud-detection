package com.ramiletus.frauddetection.service.frauddetection;

import com.ramiletus.frauddetection.persistence.model.Transaction;

public interface FraudDetectionService {
    boolean evaluateFraud(Transaction transaction);
}
