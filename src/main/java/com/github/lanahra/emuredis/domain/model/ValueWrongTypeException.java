package com.github.lanahra.emuredis.domain.model;

public class ValueWrongTypeException extends RuntimeException {

    public ValueWrongTypeException(Key key) {
        super(
                String.format(
                        "Operation against key '%s' holding the wrong kind of value", key.name()));
    }
}
