package org.example.concurrent.api;

import java.util.concurrent.Future;
import org.example.concurrent.data.Transaction;
import org.example.concurrent.data.TransactionResult;

public interface TransactionHandler {


    Future<TransactionResult> onTransaction(Transaction transaction);
}
