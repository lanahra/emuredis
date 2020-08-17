package com.github.lanahra.emuredis.domain.model;

public class Key {

    private final String name;

    public Key(String name) {
        this.name = name;
    }

    public static Key from(String name) {
        return new Key(name);
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return other != null && getClass().equals(other.getClass()) && equalsCasted((Key) other);
    }

    private boolean equalsCasted(Key other) {
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
