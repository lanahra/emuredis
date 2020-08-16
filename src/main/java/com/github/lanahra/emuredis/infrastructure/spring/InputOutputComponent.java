package com.github.lanahra.emuredis.infrastructure.spring;

import com.github.lanahra.emuredis.infrastructure.cli.InputOutputConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InputOutputComponent {

    @Bean
    public InputOutputConfiguration inputOutputConfiguration() {
        return new InputOutputConfiguration(System.in, System.out, System.err);
    }
}
