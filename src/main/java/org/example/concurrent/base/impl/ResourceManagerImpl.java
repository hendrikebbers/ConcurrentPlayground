package org.example.concurrent.base.impl;

import java.util.concurrent.Executors;
import org.example.concurrent.base.ExtendedExecutor;
import org.example.concurrent.base.ResourceManager;

public class ResourceManagerImpl implements ResourceManager {


    @Override
    public ExtendedExecutor getOrCreateExecutor(String uniqueName) {
        return new ExtendedExecutorImpl(uniqueName, true, false, Executors.newSingleThreadExecutor());
    }
}
