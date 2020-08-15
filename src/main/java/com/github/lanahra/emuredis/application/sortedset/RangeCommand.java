package com.github.lanahra.emuredis.application.sortedset;

import com.github.lanahra.emuredis.domain.model.Key;

public class RangeCommand {

    private final String key;
    private final int start;
    private final int stop;

    public RangeCommand(String key, int start, int stop) {
        this.key = key;
        this.start = start;
        this.stop = stop;
    }

    public Key key() {
        return Key.from(key);
    }

    public int start() {
        return start;
    }

    public int stop() {
        return stop;
    }
}
