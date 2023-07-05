package org.example.concurrent.pipe.impl;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.example.concurrent.base.ExtendedExecutor;

public class QueueBasedPublisher<T> extends AbstractPublisher<T> {

    private final BlockingQueue<T> queue;

    public QueueBasedPublisher(String name, ExtendedExecutor executor) {
        super(name, executor);
        queue = new LinkedBlockingQueue<>();
        if(!executor.isSingleThreaded()) {
            throw new IllegalArgumentException("Executor must be single threaded");
        }
    }

    @Override
    public void publish(T item) {
        queue.add(item);
    }

    @Override
    protected Iterator<PipeValue<T>> createIterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public PipeValue<T> next() {
                try {
                    return PipeValue.createPresent(queue.take());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
