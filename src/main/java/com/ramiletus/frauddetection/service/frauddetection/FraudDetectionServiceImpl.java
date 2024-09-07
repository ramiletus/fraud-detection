package com.ramiletus.frauddetection.service.frauddetection;

import com.ramiletus.frauddetection.persistence.dao.TransactionDao;
import com.ramiletus.frauddetection.persistence.model.PhoneNumber;
import com.ramiletus.frauddetection.persistence.model.Transaction;
import com.ramiletus.frauddetection.service.frauddetection.devicelocation.DeviceLocationVerificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class FraudDetectionServiceImpl implements FraudDetectionService {

    @Value("#{new Boolean('${location.verification.api.call}')}")
    private Boolean locationVerificationApiCall;

    private final TransactionDao transactionDao;

    private final DeviceLocationVerificationService deviceLocationVerificationService;

    public FraudDetectionServiceImpl(TransactionDao transactionDao, @Nullable DeviceLocationVerificationService deviceLocationVerificationService) {
        this.transactionDao = transactionDao;
        this.deviceLocationVerificationService = deviceLocationVerificationService;
    }

    @Override
    public int evaluateFraud(Transaction transaction) {

        String userId = transaction.getUser().getId();
        Double newLongitude = transaction.getLocation().getLon();
        Double newLatitude = transaction.getLocation().getLat();
        Long currentTimestamp = transaction .getTimestamp();
        Double maxSpeed = 150.0;

        int fraudScore = 0;

        if (locationVerificationApiCall) {
            fraudScore += deviceLocationVerificationService.verifyDeviceLocation(
                    transaction.getUser().getPhoneNumbers().stream().filter(PhoneNumber::getIsMainNumber).findFirst().get().getNumber().toString(),
                    newLongitude,
                    newLatitude,
                    transaction.getLocation().getIp());
        }

        Boolean b = transactionDao.detectFraud(userId, newLongitude, newLatitude, currentTimestamp, maxSpeed);

        if (Boolean.TRUE.equals(b)) {
            fraudScore = 99;
        }

        return fraudScore;
    }
}
