package org.example.concurrent;

import java.util.Random;
import org.example.concurrent.base.ResourceManager;
import org.example.concurrent.workflow.concurrency.TransactionExecutor;
import org.example.concurrent.workflow.TransactionHandler;
import org.example.concurrent.services.TransactionService;
import org.example.concurrent.data.Transaction;
import org.example.concurrent.base.impl.ResourceManagerImpl;
import org.example.concurrent.workflow.impl.TransactionExecutorImpl;
import org.example.concurrent.workflow.impl.TransactionHandlerImpl;
import org.example.concurrent.services.impl.TransactionServiceImpl;

public class Sample {

    public static void main(String[] args) throws Exception{
        final Random random = new Random(System.currentTimeMillis());
        ResourceManager resourceManager = new ResourceManagerImpl();


        TransactionService transactionService = new TransactionServiceImpl();
        TransactionExecutor transactionExecutor = new TransactionExecutorImpl(resourceManager);
        TransactionHandler handler = new TransactionHandlerImpl(transactionService, transactionExecutor);


        while (true) {
            Thread.sleep(random.nextLong(200));
            handler.onTransaction(new Transaction());
        }

    }
}
