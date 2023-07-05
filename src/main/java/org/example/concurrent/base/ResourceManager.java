package org.example.concurrent.base;

public interface ResourceManager {
    ExtendedExecutor getOrCreateExecutor(String uniqueName);

}
