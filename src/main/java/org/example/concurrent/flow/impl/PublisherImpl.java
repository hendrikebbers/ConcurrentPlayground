package org.example.concurrent.flow.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import org.example.concurrent.flow.api.Pipe;
import org.example.concurrent.flow.api.Publisher;

public class PublisherImpl<T> implements Publisher<T> {

    private final AtomicReference<Subscriber<? super T>> subscriber = new AtomicReference<>();

    private final AtomicBoolean completed = new AtomicBoolean(false);

    private final Executor executor;

    private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

    private final List<BlockingQueue<T>> splitterQueues = new CopyOnWriteArrayList<>();

    private final Map<T, CompletableFuture<Void>> handlingFutures = new HashMap<>();

    public PublisherImpl(Executor executor) {
        this.executor = executor;
    }

    @Override
    public Future<Void> publish(T item) {
        if(completed.get()) {
            throw new IllegalStateException("Already completed!");
        }
        queue.add(item);
        splitterQueues.forEach(q -> q.add(item));
        CompletableFuture<Void> future = new CompletableFuture<>();
        handlingFutures.put(item, future);
        return future;
    }

    @Override
    public void flush() {

    }

    @Override
    public void clear() {

    }

    @Override
    public void complete() {
        if(completed.get()) {
            throw new IllegalStateException("Already completed!");
        }
        Subscriber<? super T> subscriber = this.subscriber.get();
        if(subscriber != null) {
            executor.execute(() -> subscriber.onComplete());
        }
        handlingFutures.clear();
    }

    @Override
    public void completeExceptionally(Throwable throwable) {
        if(completed.get()) {
            throw new IllegalStateException("Already completed!");
        }
        Subscriber<? super T> subscriber = this.subscriber.get();
        if(subscriber != null) {
            executor.execute(() -> subscriber.onError(throwable));
        }
        handlingFutures.clear();
    }

    @Override
    public <U> Pipe<U> map(Function<T, U> conversion) {
        Publisher<U> publisher = new PublisherImpl<>(executor);
        subscribe(new Subscriber<T>() {

            @Override
            public void onSubscribe(Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(T item) {
                publisher.publish(conversion.apply(item));
            }


            @Override
            public void onError(Throwable throwable) {
                publisher.completeExceptionally(throwable);
            }

            @Override
            public void onComplete() {
                publisher.complete();
            }
        });
        return publisher;
    }

    @Override
    public Pipe<T> filter(Predicate<T> predicate) {
        return null;
    }

    @Override
    public void forward(Publisher<T>... publisher) {

    }

    @Override
    public Pipe<T> split() {
        final Publisher<T> splitter = new PublisherImpl<>(executor);
        final BlockingQueue<T> splitterQueue = new LinkedBlockingQueue<>();
        splitterQueues.add(splitterQueue);
        executor.execute(() -> {
            while (!completed.get()) {
                try {
                    T item = splitterQueue.take();
                    splitter.publish(item);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return splitter;
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        if(!this.subscriber.compareAndSet(null, subscriber)) {
            throw new IllegalStateException("Only one subscriber allowed!");
        }
        subscriber.onSubscribe(new Subscription() {

            @Override
            public void request(long n) {
                if(n <= 0) {
                    throw new IllegalArgumentException("n must be positive!");
                }
                executor.execute(() -> {
                    if(completed.get()) {
                        return;
                    }
                    long count = 0;
                    while (count < n) {
                        try {
                            T item = queue.take();
                            if (item == null) {
                                break;
                            }
                            CompletableFuture<Void> future = handlingFutures.remove(item);
                            if (future != null) {
                                future.complete(null);
                            }
                            subscriber.onNext(item);
                            count++;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }

            @Override
            public void cancel() {
                complete();
            }
        });
    }
}
