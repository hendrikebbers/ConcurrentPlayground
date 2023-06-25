package org.example.concurrent.workflow;

import java.util.concurrent.Future;
import org.example.concurrent.data.Transaction;
import org.example.concurrent.data.TransactionResult;

public interface TransactionWorkflow {


    Future<TransactionResult> onTransaction(Transaction transaction);
}
