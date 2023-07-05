package org.example.concurrent.base.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import org.example.concurrent.base.ExtendedExecutor;

public class ExtendedExecutorImpl implements ExtendedExecutor {

    private final boolean isSingleThreaded;

    private final boolean isVirtualThreaded;

    private final String name;

    private final Executor executor;

    public ExtendedExecutorImpl(final String name, final boolean isSingleThreaded, final boolean isVirtualThreaded, final Executor executor) {
        this.name = name;
        this.isSingleThreaded = isSingleThreaded;
        this.isVirtualThreaded = isVirtualThreaded;
        this.executor = executor;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isSingleThreaded() {
        return isSingleThreaded;
    }

    @Override
    public boolean isVirtualThreaded() {
        return isVirtualThreaded;
    }

    @Override
    public Future<Void> submit(Runnable task) {
        return submit(() -> {
            task.run();
            return null;
        });
    }

    @Override
    public <V> Future<V> submit(Callable<V> task) {
        final CompletableFuture<V> future = new CompletableFuture<>();
        execute(() -> {
            try {
                future.complete(task.call());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Override
    public void execute(Runnable command) {
        executor.execute(command);
    }
}
