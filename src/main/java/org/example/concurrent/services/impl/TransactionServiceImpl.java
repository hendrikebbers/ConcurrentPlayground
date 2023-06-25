package org.example.concurrent.services.impl;

import org.example.concurrent.services.TransactionService;
import org.example.concurrent.data.PreHandleResult;
import org.example.concurrent.data.Transaction;
import org.example.concurrent.data.TransactionResult;

public class TransactionServiceImpl implements TransactionService {

    @Override
    public PreHandleResult preHandleAccountCheck(Transaction t) {
        if(t.getAccount() == null) {
            return new PreHandleResult(true);
        }
        return new PreHandleResult(false);
    }

    @Override
    public PreHandleResult preHandleAmountCheck(Transaction t) {
        if(t.getAmount() < 0) {
            return new PreHandleResult(true);
        }
        return new PreHandleResult(false);
    }

    @Override
    public PreHandleResult preHandleHashCheck(Transaction t) {
        if(t.getHash() == null) {
            return new PreHandleResult(true);
        }
        return new PreHandleResult(false);
    }

    @Override
    public TransactionResult handleTransaction(Transaction t) {
        System.out.println("Handling " + t);
        return new TransactionResult(false);
    }
}
