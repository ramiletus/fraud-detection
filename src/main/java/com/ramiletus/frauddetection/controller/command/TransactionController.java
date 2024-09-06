package com.ramiletus.frauddetection.controller.command;

import com.ramiletus.frauddetection.persistence.model.Transaction;
import com.ramiletus.frauddetection.service.transaction.TransactionCommandHandler;
import com.ramiletus.frauddetection.service.transaction.registertransaction.RegisterTransactionCommand;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.InstanceNotFoundException;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionCommandHandler transactionCommandHandler;

    public TransactionController(TransactionCommandHandler transactionCommandHandler) {
        this.transactionCommandHandler = transactionCommandHandler;
    }
    @PostMapping(value = "/inject")
    public ResponseEntity<Transaction> injectUsers(@Valid @RequestBody RegisterTransactionCommand registerTransactionCommand) throws InstanceNotFoundException {
        return ResponseEntity.ok(transactionCommandHandler.handle(registerTransactionCommand));
    }
}
