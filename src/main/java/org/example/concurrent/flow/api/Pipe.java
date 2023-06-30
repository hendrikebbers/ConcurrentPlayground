package org.example.concurrent.flow.api;

import java.util.concurrent.Flow;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Pipe<T> extends Flow.Publisher<T> {

    /**
     * Do we really need that? PERFORMANCE
     * @return
     */
    Pipe<T> split();

    <U> Pipe<U> map(Function<T, U> conversion);

    Pipe<T> filter(Predicate<T> predicate);

    void forward(Publisher<T>... publisher);
}
