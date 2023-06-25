package org.example.concurrent.impl;

import java.util.concurrent.Future;
import org.example.concurrent.api.TransactionExecutor;
import org.example.concurrent.api.TransactionHandler;
import org.example.concurrent.api.TransactionService;
import org.example.concurrent.data.CombinedPreHandleResult;
import org.example.concurrent.data.PreHandleResult;
import org.example.concurrent.data.Transaction;
import org.example.concurrent.data.TransactionResult;

public class TransactionHandlerImpl implements TransactionHandler {

    private final TransactionExecutor executor;

    private final TransactionService transactionService;

    public TransactionHandlerImpl(TransactionService transactionService, TransactionExecutor executor) {
        this.transactionService = transactionService;
        this.executor = executor;
    }

    public Future<TransactionResult> onTransaction(Transaction transaction) {
        Future<PreHandleResult> preHandleAccount = executor.preHandleCustomTask(t -> transactionService.preHandleAccountCheck(t), transaction);
        Future<PreHandleResult> preHandleAmount = executor.preHandleCustomTask(t -> transactionService.preHandleAmountCheck(t), transaction);
        Future<PreHandleResult> preHandleHash = executor.preHandleCustomTask(t -> transactionService.preHandleHashCheck(t), transaction);

        Future<CombinedPreHandleResult> preHandleTask = executor.combinePreHandle(preHandleAccount, preHandleAmount, preHandleHash);

        Future<TransactionResult> transactionHandleTask = executor.handle(t -> transactionService.handleTransaction(t), preHandleTask, transaction);
        return transactionHandleTask;
    }
}
