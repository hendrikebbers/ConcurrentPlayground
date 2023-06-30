package org.example.concurrent.future.services;

import org.example.concurrent.future.data.PreHandleResult;
import org.example.concurrent.future.data.Transaction;
import org.example.concurrent.future.data.TransactionResult;

public interface TransactionService {

    PreHandleResult preHandleAccountCheck(Transaction t);

    PreHandleResult preHandleAmountCheck(Transaction t);

    PreHandleResult preHandleHashCheck(Transaction t);

    TransactionResult handleTransaction(Transaction t);

}
