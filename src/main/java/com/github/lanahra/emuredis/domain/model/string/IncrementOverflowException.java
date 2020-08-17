package com.github.lanahra.emuredis.domain.model.string;

public class IncrementOverflowException extends RuntimeException {

    public IncrementOverflowException() {
        super("Increment or decrement would overflow");
    }
}
