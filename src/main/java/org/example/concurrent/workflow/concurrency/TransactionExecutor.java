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

    //Execute the given check for the transaction on the preHandle executor (Thread pool)
    Future<PreHandleResult> preHandleCustomTask(Function<Transaction, PreHandleResult> task, Transaction t);

    //Return a future that is done once all given futures are done
    Future<CombinedPreHandleResult> combinePreHandle(Future<PreHandleResult>... preHandleTasks);


    Future<TransactionResult> handle(
            Function<Transaction, TransactionResult> handling, Future<CombinedPreHandleResult> preHandleTask, Transaction t);

}
