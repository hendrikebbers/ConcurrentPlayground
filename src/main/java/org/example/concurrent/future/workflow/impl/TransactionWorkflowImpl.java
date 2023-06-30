package org.example.concurrent.future.workflow.impl;

import java.util.concurrent.Future;
import org.example.concurrent.future.workflow.TransactionWorkflow;
import org.example.concurrent.future.workflow.concurrency.TransactionExecutor;
import org.example.concurrent.future.services.TransactionService;
import org.example.concurrent.future.data.CombinedPreHandleResult;
import org.example.concurrent.future.data.PreHandleResult;
import org.example.concurrent.future.data.Transaction;
import org.example.concurrent.future.data.TransactionResult;

public class TransactionWorkflowImpl implements TransactionWorkflow {

    private final TransactionExecutor executor;

    private final TransactionService transactionService;

    public TransactionWorkflowImpl(TransactionService transactionService, TransactionExecutor executor) {
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
