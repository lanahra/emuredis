package com.github.lanahra.emuredis.infrastructure.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineParser {

    public static final Pattern PATTERN = Pattern.compile("\"([^\"]*)\"|(\\S+)");

    private LineParser() {
        // utility class
    }

    public static String[] parseLine(String line) {
        Matcher matcher = PATTERN.matcher(line);
        List<String> words = new ArrayList<>();
        while (matcher.find()) {
            String quotedWord = matcher.group(1);
            String word = matcher.group(2);
            words.add(quotedWord == null ? word : quotedWord);
        }
        return words.toArray(String[]::new);
    }
}
