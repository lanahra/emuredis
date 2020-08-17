package com.github.lanahra.emuredis.domain.model;

public class KeyNotFoundException extends RuntimeException {

    public KeyNotFoundException(Key key) {
        super(String.format("Key not found: '%s'", key.name()));
    }
}
