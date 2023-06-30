package org.example.concurrent.pipe.api;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Pipe<T> {

    <U> Pipe<U> map(Function<T, U> conversion);

    Pipe<T> filter(Predicate<T> predicate);

    Pipe<T> peek(Consumer<T> consumer);

    void forEach(Consumer<T> consumer);

    void forward(Publisher<T>... publisher);

}
