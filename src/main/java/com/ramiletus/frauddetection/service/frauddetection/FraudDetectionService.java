package com.ramiletus.frauddetection.service.frauddetection;

import com.ramiletus.frauddetection.persistence.model.Transaction;

public interface FraudDetectionService {
    int evaluateFraud(Transaction transaction);
}
