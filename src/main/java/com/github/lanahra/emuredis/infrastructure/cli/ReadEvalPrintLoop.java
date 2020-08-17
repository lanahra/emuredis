package com.github.lanahra.emuredis.infrastructure.cli;

import static com.github.lanahra.emuredis.infrastructure.cli.Message.REPLY_OK;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ReadEvalPrintLoop {

    private static final String OUTPUT_ERR = "(error)";
    private static final String COMMAND_QUIT = "quit";

    private final InputStream in;
    private final PrintStream out;
    private final PrintStream err;
    private final Parser parser;

    public ReadEvalPrintLoop(InputOutputConfiguration configuration, Parser parser) {
        this.in = configuration.in();
        this.out = configuration.out();
        this.err = configuration.err();
        this.parser = parser;
    }

    public void start() {
        Scanner scanner = new Scanner(in);
        while (scanner.hasNextLine()) {
            String[] args = LineParser.parseLine(scanner.nextLine());
            if (isQuitCommand(args)) {
                print(REPLY_OK);
                break;
            }
            String output = parser.parse(args);
            print(output);
        }
    }

    private boolean isQuitCommand(String[] args) {
        return args.length > 0 && args[0].equalsIgnoreCase(COMMAND_QUIT);
    }

    private void print(String output) {
        if (output.startsWith(OUTPUT_ERR)) {
            err.println(output);
        } else {
            out.println(output);
        }
    }
}
