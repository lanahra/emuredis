package com.github.lanahra.emuredis.application;

import java.util.function.Supplier;

public class SimpleTransaction implements Transaction {

    @Override
    public <T> T execute(Runnable action) {
        action.run();
        return null;
    }

    @Override
    public <T> T execute(Supplier<T> action) {
        return action.get();
    }
}