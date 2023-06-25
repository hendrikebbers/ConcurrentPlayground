package org.example.concurrent.impl;

import org.example.concurrent.api.TransactionService;
import org.example.concurrent.data.PreHandleResult;
import org.example.concurrent.data.Transaction;
import org.example.concurrent.data.TransactionResult;

public class TransactionServiceImpl implements TransactionService {

    @Override
    public PreHandleResult preHandleAccountCheck(Transaction t) {
        return new PreHandleResult(false);
    }

    @Override
    public PreHandleResult preHandleAmountCheck(Transaction t) {
        return new PreHandleResult(false);
    }

    @Override
    public PreHandleResult preHandleHashCheck(Transaction t) {
        return new PreHandleResult(false);
    }

    @Override
    public TransactionResult handleTransaction(Transaction t) {
        return new TransactionResult(false);
    }
}
