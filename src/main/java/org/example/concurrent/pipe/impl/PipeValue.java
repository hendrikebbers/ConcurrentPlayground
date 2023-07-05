package org.example.concurrent.pipe.impl;

record PipeValue<T>(T value, boolean isPresent) {

    static <T> PipeValue<T> createNotPresent() {
        return new PipeValue<>(null, false);
    }

    static <T> PipeValue<T> createPresent(T value) {
        return new PipeValue<>(value, true);
    }
}
