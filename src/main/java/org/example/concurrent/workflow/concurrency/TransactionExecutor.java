package org.example.concurrent.workflow.concurrency;

import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import org.example.concurrent.data.CombinedPreHandleResult;
import org.example.concurrent.data.PreHandleResult;
import org.example.concurrent.data.Transaction;
import org.example.concurrent.data.TransactionResult;

public interface TransactionExecutor {

    //Execute the account check for the transaction on the preHandle executor (Thread pool)
    Future<PreHandleResult> preHandleCheckAccount(Transaction t);

    //Execute the amount check for the transaction on the preHandle executor (Thread pool)
    Future<PreHandleResult> preHandleCheckValidAmount(Transaction t);

    //Execute the hash check for the transaction on the preHandle executor (Thread pool)
    Future<PreHandleResult> preHandleCheckHash(Transaction t);

    //Execute the given check for the transaction on the preHandle executor (Thread pool)
    Future<PreHandleResult> preHandleCustomTask(Function<Transaction, PreHandleResult> task, Transaction t);

    //Return a future that is done once all given futures are done
    Future<CombinedPreHandleResult> combinePreHandle(Future<PreHandleResult>... preHandleTasks);

    //executes the handling on the transaction on the transaction executor (singe thread) once pre handle is done
    Future<TransactionResult> handle(Future<CombinedPreHandleResult> preHandleTask, Transaction t);

    Future<TransactionResult> handle(
            Function<Transaction, TransactionResult> handling, Future<CombinedPreHandleResult> preHandleTask, Transaction t);

}
