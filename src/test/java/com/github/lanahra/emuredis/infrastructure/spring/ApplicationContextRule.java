package com.github.lanahra.emuredis.infrastructure.spring;

import org.junit.rules.ExternalResource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spark.Service;

public class ApplicationContextRule extends ExternalResource {

    private ApplicationContext applicationContext;

    @Override
    protected void before() {
        this.applicationContext =
                new AnnotationConfigApplicationContext(IntegrationTestComponent.class);
        Service service = applicationContext.getBean(Service.class);
        service.init();
        service.awaitInitialization();
    }

    @Override
    protected void after() {
        Service service = applicationContext.getBean(Service.class);
        service.stop();
        service.awaitStop();
    }

    public <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }
}
