package org.example.concurrent.base.impl;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.example.concurrent.base.ResourceManager;

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
