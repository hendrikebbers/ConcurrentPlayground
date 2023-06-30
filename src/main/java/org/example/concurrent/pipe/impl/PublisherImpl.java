package org.example.concurrent.pipe.impl;

import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.example.concurrent.pipe.api.Pipe;
import org.example.concurrent.pipe.api.Publisher;

public class PublisherImpl<T> implements Publisher<T> {

    private final Executor executor;

    public PublisherImpl(Executor executor) {
        this.executor = executor;
    }

    @Override
    public <U> Pipe<U> map(Function<T, U> conversion) {
        return null;
    }

    @Override
    public Pipe<T> filter(Predicate<T> predicate) {
        return null;
    }

    @Override
    public Pipe<T> peek(Consumer<T> consumer) {
        return null;
    }

    @Override
    public void forEach(Consumer<T> consumer) {

    }

    @Override
    public void forward(Publisher<T>... publisher) {

    }

    @Override
    public void publish(T item) {

    }
}
