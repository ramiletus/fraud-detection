package com.ramiletus.frauddetection.service.transaction;

import com.ramiletus.frauddetection.persistence.model.Transaction;
import com.ramiletus.frauddetection.service.transaction.registertransaction.RegisterTransactionCommand;

import javax.management.InstanceNotFoundException;

public interface TransactionCommandHandler{
    Transaction handleTransaction(RegisterTransactionCommand command) throws InstanceNotFoundException;
}
