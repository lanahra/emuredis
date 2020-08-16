package com.github.lanahra.emuredis.application;

import java.util.function.Supplier;

public class SimpleTransaction implements Transaction {


    @Override
    public void run(Runnable action) {
        action.run();
    }

    @Override
    public <T> T supply(Supplier<T> action) {
        return action.get();
    }
}