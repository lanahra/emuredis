package com.github.lanahra.emuredis.application;

import java.util.function.Supplier;

public interface Transaction {

    void run(Runnable action);

    <T> T supply(Supplier<T> action);
}
