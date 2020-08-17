package com.github.lanahra.emuredis;

import com.github.lanahra.emuredis.infrastructure.cli.ReadEvalPrintLoop;
import com.github.lanahra.emuredis.infrastructure.spring.ApiComponent;
import com.github.lanahra.emuredis.infrastructure.spring.CommandLineComponent;
import com.github.lanahra.emuredis.infrastructure.spring.CoreComponent;
import com.github.lanahra.emuredis.infrastructure.spring.InputOutputComponent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher {

    private final AnnotationConfigApplicationContext applicationContext;

    public Launcher() {
        this.applicationContext = new AnnotationConfigApplicationContext();
    }

    public static Launcher launcher() {
        return new Launcher();
    }

    public <T> Launcher withComponent(Class<T> componentClass) {
        applicationContext.register(componentClass);
        return this;
    }

    public void launch() {
        applicationContext.refresh();
        ReadEvalPrintLoop repl = applicationContext.getBean(ReadEvalPrintLoop.class);
        repl.start();
    }

    public static void main(String[] args) {
        launcher()
                .withComponent(CoreComponent.class)
                .withComponent(InputOutputComponent.class)
                .withComponent(CommandLineComponent.class)
                .withComponent(ApiComponent.class)
                .launch();
    }
}
