package com.github.lanahra.emuredis.infrastructure.spring;

import com.github.lanahra.emuredis.infrastructure.cli.CommandParser;
import com.github.lanahra.emuredis.infrastructure.cli.InputOutputConfiguration;
import com.github.lanahra.emuredis.infrastructure.cli.ReadEvalPrintLoop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InputOutputComponent {

    private final CommandParser commandParser;

    public InputOutputComponent(CommandParser commandParser) {
        this.commandParser = commandParser;
    }

    @Bean
    public InputOutputConfiguration inputOutputConfiguration() {
        return new InputOutputConfiguration(System.in, System.out, System.err);
    }

    @Bean
    public ReadEvalPrintLoop readEvalPrintLoop() {
        return new ReadEvalPrintLoop(inputOutputConfiguration(), commandParser);
    }
}
