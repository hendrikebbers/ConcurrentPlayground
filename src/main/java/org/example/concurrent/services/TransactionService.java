package org.example.concurrent.services;

import org.example.concurrent.data.PreHandleResult;
import org.example.concurrent.data.Transaction;
import org.example.concurrent.data.TransactionResult;

public interface TransactionService {

    PreHandleResult preHandleAccountCheck(Transaction t);

    PreHandleResult preHandleAmountCheck(Transaction t);

    PreHandleResult preHandleHashCheck(Transaction t);

    TransactionResult handleTransaction(Transaction t);

}
