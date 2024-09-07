package com.ramiletus.frauddetection.service.transaction;

import com.ramiletus.frauddetection.persistence.dao.DeviceDao;
import com.ramiletus.frauddetection.persistence.dao.LocationDao;
import com.ramiletus.frauddetection.persistence.dao.TransactionDao;
import com.ramiletus.frauddetection.persistence.dao.UserDao;
import com.ramiletus.frauddetection.persistence.model.Device;
import com.ramiletus.frauddetection.persistence.model.Location;
import com.ramiletus.frauddetection.persistence.model.Transaction;
import com.ramiletus.frauddetection.persistence.model.User;
import com.ramiletus.frauddetection.service.frauddetection.FraudDetectionService;
import com.ramiletus.frauddetection.service.transaction.registertransaction.RegisterTransactionCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;

@Service
@AllArgsConstructor
public class TransactionCommandHandlerImpl implements TransactionCommandHandler {

    private final TransactionDao transactionDao;
    private final LocationDao locationDao;
    private final DeviceDao deviceDao;
    private final UserDao userDao;
    private final FraudDetectionService fraudDetectionService;

    @Override
    public Transaction handle(RegisterTransactionCommand command) throws InstanceNotFoundException {

        User user = userDao.findById(command.getUserId()).orElseThrow(() -> new InstanceNotFoundException("User not found"));

        Device device = deviceDao.findById(command.getDeviceId()).orElseThrow(() -> new InstanceNotFoundException("Device not found"));

        Location location = locationDao.findById(command.getLocationId()).orElseThrow(() -> new InstanceNotFoundException("Location not found"));

        Transaction transactionToInsert = new Transaction();
        transactionToInsert.setUser(user);
        transactionToInsert.setDevice(device);
        transactionToInsert.setLocation(location);
        transactionToInsert.setTimestamp(command.getTimestamp());

        int isFraud = fraudDetectionService.evaluateFraud(transactionToInsert);

        transactionToInsert.setIsFraud(isFraud);

        return transactionDao.save(transactionToInsert);
    }
}
