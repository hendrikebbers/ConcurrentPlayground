package org.example.concurrent.pipe.api;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public interface Pipe<T> {

    <U> Pipe<U> map(Function<T, U> conversion);

    Pipe<T> filter(Predicate<T> predicate);

    Pipe<T> peek(Consumer<T> consumer);

    void forEach(Consumer<T> consumer);

    default void forward(Publisher<T>... publisher) {
        forEach(item -> {
            for (Publisher<T> p : publisher) {
                p.publish(item);
            }
        });
    }

    default Pipe<List<T>> buffer(int bufferSize) {
        return buffer(Collectors.toList(), bufferSize);
    }

    <R, U> Pipe<U> buffer(Collector<T, R, U> collector, int bufferSize);
}
