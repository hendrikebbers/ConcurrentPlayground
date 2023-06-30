package org.example.concurrent.flow.api;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;

@FunctionalInterface
public interface SimpleSubscriber<T> extends Flow.Subscriber<T> {

    @Override
    default void onSubscribe(Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    default void onError(Throwable throwable) {
        throw new RuntimeException("Error in Flow!", throwable);
    }

    @Override
    default void onComplete() {}
}
