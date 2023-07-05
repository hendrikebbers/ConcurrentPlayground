package org.example.concurrent.future.workflow.impl;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;
import org.example.concurrent.base.ResourceManager;
import org.example.concurrent.future.workflow.concurrency.TransactionExecutor;
import org.example.concurrent.future.data.CombinedPreHandleResult;
import org.example.concurrent.future.data.PreHandleResult;
import org.example.concurrent.future.data.Transaction;
import org.example.concurrent.future.data.TransactionResult;

public class TransactionExecutorImpl implements TransactionExecutor {

    private final ResourceManager resourceManager;

    private Executor preHandleExecutor;

    private Executor handleExecutor;

    public TransactionExecutorImpl(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        preHandleExecutor = resourceManager.getOrCreateExecutor("preHandleExecutor");
        handleExecutor = resourceManager.getOrCreateExecutor("handleExecutor");
    }


    @Override
    public Future<PreHandleResult> preHandleCustomTask(Function<Transaction, PreHandleResult> task, Transaction t) {
        return preHandleCustomTask(() -> task.apply(t));
    }

    public Future<PreHandleResult> preHandleCustomTask(Supplier<PreHandleResult> task) {
        return CompletableFuture.supplyAsync(task, preHandleExecutor);
    }

    @Override
    public Future<CombinedPreHandleResult> combinePreHandle(Future<PreHandleResult>... preHandleTasks) {
        return CompletableFuture.supplyAsync(() -> {
            boolean error = Arrays.stream(preHandleTasks).anyMatch(t -> {
                try {
                    PreHandleResult preHandleResult = t.get();
                    if(!preHandleResult.fail()) {
                        return true;
                    }
                } catch (Exception e) {
                    return true;
                }
                return false;
            });
            if(error) {
                return new CombinedPreHandleResult(true);
            }
            return new CombinedPreHandleResult(false);
        }, preHandleExecutor);
    }

    @Override
    public Future<TransactionResult> handle(Function<Transaction, TransactionResult> handling, Future<CombinedPreHandleResult> preHandleTask, Transaction t) {
        if (preHandleTask instanceof CompletableFuture<CombinedPreHandleResult> cf) {
            return cf.handleAsync((result, exception) -> {
                if (exception != null) {
                    return new TransactionResult(true);
                }
                if(!result.fail()) {
                    return new TransactionResult(true);
                }
                return handling.apply(t);
            }, handleExecutor);
        }
        return CompletableFuture.supplyAsync(() -> {
            try {
                return preHandleTask.get();
            } catch (Exception e) {
                return new CombinedPreHandleResult(true);
            }
        }, preHandleExecutor).handleAsync((result, exception) -> {
            if (exception != null) {
                return new TransactionResult(true);
            }
            if(!result.fail()) {
                return new TransactionResult(true);
            }
            return handling.apply(t);
        }, handleExecutor);
    }
}
