package org.example.concurrent.base;

import java.util.concurrent.Executor;

public interface ResourceManager {
    Executor createPooledExecutor();

    Executor createSingleThreadExecutor();
}
