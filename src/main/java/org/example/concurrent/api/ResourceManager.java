package org.example.concurrent.api;

import java.util.concurrent.Executor;

public interface ResourceManager {
    Executor createPooledExecutor();

    Executor createSingleThreadExecutor();
}
