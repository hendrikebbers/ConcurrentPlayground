package org.example.concurrent.base;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public interface ExtendedExecutor extends Executor {

    String getName();

    boolean isSingleThreaded();

    boolean isVirtualThreaded();

    Future<Void> submit(Runnable task);

    <V> Future<V> submit(Callable<V> task);
}
