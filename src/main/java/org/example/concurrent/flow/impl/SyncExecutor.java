package org.example.concurrent.flow.impl;

import java.util.Objects;
import java.util.concurrent.Executor;

public class SyncExecutor implements Executor {

    private final static SyncExecutor INSTANCE = new SyncExecutor();

    private SyncExecutor() {
    }

    @Override
    public void execute(Runnable command) {
        Objects.requireNonNull(command, "command must not be null");
        command.run();
    }

    public static SyncExecutor getInstance() {
        return INSTANCE;
    }

}
