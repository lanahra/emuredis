package com.github.lanahra.emuredis.infrastructure.cli;

import java.io.InputStream;
import java.io.PrintStream;

public class InputOutputConfiguration {

    private final InputStream in;
    private final PrintStream out;
    private final PrintStream err;

    public InputOutputConfiguration(InputStream in, PrintStream out, PrintStream err) {
        this.in = in;
        this.out = out;
        this.err = err;
    }

    public InputStream in() {
        return in;
    }

    public PrintStream out() {
        return out;
    }

    public PrintStream err() {
        return err;
    }
}
