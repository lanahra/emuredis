package com.github.lanahra.emuredis.domain.model.string;

public class IntegerRepresentationException extends RuntimeException {

    public IntegerRepresentationException(Throwable cause) {
        super("ERR value is not an integer or out of range", cause);
    }
}
