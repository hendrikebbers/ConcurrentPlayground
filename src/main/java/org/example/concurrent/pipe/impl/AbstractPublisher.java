package org.example.concurrent.pipe.impl;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import org.example.concurrent.base.ExtendedExecutor;
import org.example.concurrent.pipe.api.Pipe;
import org.example.concurrent.pipe.api.Publisher;

public abstract class AbstractPublisher<T> implements Publisher<T> {

    public final String name;

    private final PipeImpl<T> innerPipe;

    public AbstractPublisher(String name, ExtendedExecutor executor) {
        this.name = name;
        this.innerPipe = new PipeImpl<>(name, executor, createIterator());
    }

    protected abstract Iterator<PipeValue<T>> createIterator();

    @Override
    public <U> Pipe<U> map(Function<T, U> conversion) {
        return innerPipe.map(conversion);
    }

    @Override
    public Pipe<T> filter(Predicate<T> predicate) {
        return innerPipe.filter(predicate);
    }

    @Override
    public Pipe<T> peek(Consumer<T> consumer) {
        return innerPipe.peek(consumer);
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        innerPipe.forEach(consumer);
    }

    @Override
    public <R, U> Pipe<U> buffer(Collector<T, R, U> collector, int bufferSize) {
        return innerPipe.buffer(collector, bufferSize);
    }
}
