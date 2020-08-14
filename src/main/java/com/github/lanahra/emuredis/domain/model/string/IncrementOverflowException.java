package com.github.lanahra.emuredis.domain.model.string;

public class IncrementOverflowException extends RuntimeException {

    public IncrementOverflowException() {
        super("ERR increment or decrement would overflow");
    }
}
