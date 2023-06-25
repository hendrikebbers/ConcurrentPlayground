package org.example.concurrent.impl;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.example.concurrent.api.ResourceManager;

public class ResourceManagerImpl implements ResourceManager {

    @Override
    public Executor createPooledExecutor() {
        return Executors.newCachedThreadPool();
    }

    @Override
    public Executor createSingleThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
