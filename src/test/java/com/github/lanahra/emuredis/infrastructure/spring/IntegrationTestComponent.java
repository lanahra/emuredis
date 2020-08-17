package com.github.lanahra.emuredis.infrastructure.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CoreComponent.class, CommandLineComponent.class, ApiComponent.class})
public class IntegrationTestComponent {
    // component class
}
