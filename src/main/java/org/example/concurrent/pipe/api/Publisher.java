package org.example.concurrent.pipe.api;

public interface Publisher<T> extends Pipe<T> {

    void publish(T item);
}
