package org.example.concurrent.flow.api;

import java.util.concurrent.Future;

public interface Publisher<T> extends Pipe<T> {

    Future<Void> publish(T item);

    /**
     * Blocking method that is waiting until everything that has been published before is progressed.
     */
    void flush();

    /**
     * All items that have been published so far will be removed. Not clear now if we need it.
     * Cody / Lazar need to discuss if flush is good enough
     */
    void clear();

    void complete();

    void completeExceptionally(Throwable throwable);

}
