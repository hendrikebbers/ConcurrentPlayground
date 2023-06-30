package org.example.concurrent.future;

import java.util.Random;
import org.example.concurrent.base.ResourceManager;
import org.example.concurrent.future.workflow.concurrency.TransactionExecutor;
import org.example.concurrent.future.workflow.TransactionWorkflow;
import org.example.concurrent.future.services.TransactionService;
import org.example.concurrent.future.data.Transaction;
import org.example.concurrent.base.impl.ResourceManagerImpl;
import org.example.concurrent.future.workflow.impl.TransactionExecutorImpl;
import org.example.concurrent.future.workflow.impl.TransactionWorkflowImpl;
import org.example.concurrent.future.services.impl.TransactionServiceImpl;

public class Sample {

    public static void main(String[] args) throws Exception{
        final Random random = new Random(System.currentTimeMillis());
        ResourceManager resourceManager = new ResourceManagerImpl();


        TransactionService transactionService = new TransactionServiceImpl();
        TransactionExecutor transactionExecutor = new TransactionExecutorImpl(resourceManager);
        TransactionWorkflow workflow = new TransactionWorkflowImpl(transactionService, transactionExecutor);


        while (true) {
            Thread.sleep(random.nextLong(200));
            workflow.onTransaction(new Transaction());
        }
    }
}
