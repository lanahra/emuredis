package com.github.lanahra.emuredis.infrastructure.spring;

import com.github.lanahra.emuredis.application.KeyApplicationService;
import com.github.lanahra.emuredis.application.sortedset.SortedSetApplicationService;
import com.github.lanahra.emuredis.application.string.StringApplicationService;
import com.github.lanahra.emuredis.infrastructure.cli.CommandParser;
import com.github.lanahra.emuredis.infrastructure.cli.InputOutputConfiguration;
import com.github.lanahra.emuredis.infrastructure.cli.KeyCommandParser;
import com.github.lanahra.emuredis.infrastructure.cli.ReadEvalPrintLoop;
import com.github.lanahra.emuredis.infrastructure.cli.SortedSetCommandParser;
import com.github.lanahra.emuredis.infrastructure.cli.StringCommandParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandLineComponent {

    private final InputOutputConfiguration inputOutputConfiguration;
    private final KeyApplicationService keyService;
    private final StringApplicationService stringService;
    private final SortedSetApplicationService sortedSetService;

    public CommandLineComponent(
            InputOutputConfiguration inputOutputConfiguration,
            KeyApplicationService keyService,
            StringApplicationService stringService,
            SortedSetApplicationService sortedSetService) {
        this.inputOutputConfiguration = inputOutputConfiguration;
        this.keyService = keyService;
        this.stringService = stringService;
        this.sortedSetService = sortedSetService;
    }

    @Bean
    public ReadEvalPrintLoop readEvalPrintLoop() {
        return new ReadEvalPrintLoop(inputOutputConfiguration, commandParser());
    }

    @Bean
    public CommandParser commandParser() {
        return new CommandParser(
                keyCommandParser(), stringCommandParser(), sortedSetCommandParser());
    }

    @Bean
    public KeyCommandParser keyCommandParser() {
        return new KeyCommandParser(keyService);
    }

    @Bean
    public StringCommandParser stringCommandParser() {
        return new StringCommandParser(stringService);
    }

    @Bean
    public SortedSetCommandParser sortedSetCommandParser() {
        return new SortedSetCommandParser(sortedSetService);
    }
}
