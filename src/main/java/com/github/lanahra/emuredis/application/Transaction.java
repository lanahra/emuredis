package com.github.lanahra.emuredis.application;

import java.util.function.Supplier;

public interface Transaction {

    <T> T execute(Runnable action);

    <T> T execute(Supplier<T> action);
}
