package com.github.lanahra.emuredis.infrastructure.transaction;

import com.github.lanahra.emuredis.application.Transaction;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class ExecutorTransaction implements Transaction {

    private final Executor executor;

    public ExecutorTransaction(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void run(Runnable action) {
        CompletableFuture.runAsync(action, executor);
    }

    @Override
    public <T> T supply(Supplier<T> action) {
        try {
            return CompletableFuture.supplyAsync(action, executor).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (ExecutionException e) {
            throw (RuntimeException) e.getCause();
        }
    }
}
