package org.example.concurrent.future.workflow;

import java.util.concurrent.Future;
import org.example.concurrent.future.data.Transaction;
import org.example.concurrent.future.data.TransactionResult;

public interface TransactionWorkflow {


    Future<TransactionResult> onTransaction(Transaction transaction);
}
