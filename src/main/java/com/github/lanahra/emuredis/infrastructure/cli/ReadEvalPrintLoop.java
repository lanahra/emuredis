package com.github.lanahra.emuredis.infrastructure.cli;

import static com.github.lanahra.emuredis.infrastructure.cli.Message.REPLY_OK;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadEvalPrintLoop {

    public static final Pattern PATTERN = Pattern.compile("\"([^\"]*)\"|(\\S+)");

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
            String[] args = parseLine(scanner.nextLine());
            if (isQuitCommand(args)) {
                print(REPLY_OK);
                break;
            }
            String output = parser.parse(args);
            print(output);
        }
    }

    private String[] parseLine(String line) {
        Matcher matcher = PATTERN.matcher(line);
        List<String> words = new ArrayList<>();
        while (matcher.find()) {
            String quotedWord = matcher.group(1);
            String word = matcher.group(2);
            words.add(quotedWord == null ? word : quotedWord);
        }
        return words.toArray(String[]::new);
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
