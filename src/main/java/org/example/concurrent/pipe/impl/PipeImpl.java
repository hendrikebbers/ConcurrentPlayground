package org.example.concurrent.pipe.impl;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import org.example.concurrent.base.ExtendedExecutor;
import org.example.concurrent.pipe.api.Pipe;

public class PipeImpl<T> implements Pipe<T> {

    private final ExtendedExecutor executor;

    public final String name;

    Iterator<PipeValue<T>> iterator;

    public PipeImpl(String name, ExtendedExecutor executor, Iterator<PipeValue<T>> iterator) {
        this.name = name;
        this.executor = executor;
        this.iterator = iterator;
    }

    @Override
    public <U> Pipe<U> map(Function<T, U> conversion) {
        Supplier<PipeValue<U>> nextSupplier = () -> {
            final PipeValue<T> value = iterator.next();
            if(value.isPresent()) {
                return PipeValue.createPresent(conversion.apply(value.value()));
            }
            return PipeValue.createNotPresent();
        };
        return new PipeImpl<>(name, executor, new PipeIterator<>(nextSupplier, iterator::hasNext));
    }

    @Override
    public Pipe<T> filter(Predicate<T> predicate) {
        Supplier<PipeValue<T>> nextSupplier = () -> {
            final PipeValue<T> value = iterator.next();
            if(value.isPresent()) {
                if(predicate.test(value.value())) {
                    return value;
                }
            }
            return PipeValue.createNotPresent();
        };
        return new PipeImpl<>(name, executor, new PipeIterator<>(nextSupplier, iterator::hasNext));
    }

    @Override
    public Pipe<T> peek(Consumer<T> consumer) {
        Supplier<PipeValue<T>> nextSupplier = () -> {
            final PipeValue<T> value = iterator.next();
            if(value.isPresent()) {
                consumer.accept(value.value());
            }
            return value;
        };
        return new PipeImpl<>(name, executor, new PipeIterator<>(nextSupplier, iterator::hasNext));
    }

    @Override
    public <A, R> Pipe<R> buffer(Collector<T, A, R> collector, int bufferSize) {
        AtomicInteger counter = new AtomicInteger(0);
        Supplier<PipeValue<R>> nextSupplier = () -> {
            final PipeValue<T> value = iterator.next();
            if(value.isPresent()) {
                if (counter.get() < bufferSize) {
                    counter.incrementAndGet();
                    collector.accumulator().accept(collector.supplier().get(), value.value());
                } else {
                    counter.set(0);
                    return PipeValue.createPresent(collector.finisher().apply(collector.supplier().get()));
                }
            }
            return PipeValue.createNotPresent();
        };
        return new PipeImpl<>(name, executor, new PipeIterator<>(nextSupplier, iterator::hasNext));
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        executor.execute(() -> {
            while(iterator.hasNext()) {
                final PipeValue<T> value = iterator.next();
                if(value.isPresent()) {
                    consumer.accept(value.value());
                }
            }
        });
    }

    private class PipeIterator<T> implements Iterator<PipeValue<T>> {

        private final Supplier<PipeValue<T>> supplier;

        public final Supplier<Boolean> hasNextSupplier;

        public PipeIterator(Supplier<PipeValue<T>> supplier, Supplier<Boolean> hasNextSupplier) {
            this.supplier = supplier;
            this.hasNextSupplier = hasNextSupplier;
        }

        public PipeIterator(Supplier<PipeValue<T>> supplier, PipeIterator<T> baseIterator) {
            this.supplier = supplier;
            this.hasNextSupplier = () -> baseIterator.hasNext();
        }

        @Override
        public boolean hasNext() {
            return hasNextSupplier.get();
        }

        @Override
        public PipeValue<T> next() {
            return supplier.get();
        }
    }
}
