package com.github.lanahra.emuredis.infrastructure.spring;

import com.github.lanahra.emuredis.infrastructure.api.CommandApi;
import com.github.lanahra.emuredis.infrastructure.api.KeyCommandApi;
import com.github.lanahra.emuredis.infrastructure.api.QueryParamRequiredException;
import com.github.lanahra.emuredis.infrastructure.api.SortedSetCommandApi;
import com.github.lanahra.emuredis.infrastructure.api.StringCommandApi;
import com.github.lanahra.emuredis.infrastructure.cli.CommandParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spark.Request;
import spark.Response;
import spark.Service;

@Configuration
public class ApiComponent {

    private static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

    private static final Logger logger = LoggerFactory.getLogger(ApiComponent.class);

    private final CommandParser commandParser;

    public ApiComponent(CommandParser commandParser) {
        this.commandParser = commandParser;
    }

    @Bean
    public Service service() {
        Service service = Service.ignite();
        service.port(8080);
        service.exception(Exception.class, this::internalServerError);
        service.before((request, response) -> response.type(CONTENT_TYPE_TEXT_PLAIN));
        service.after(this::formatResponse);
        service.exception(QueryParamRequiredException.class, this::formatErrorResponse);
        return service;
    }

    private void formatResponse(Request request, Response response) {
        if (response.body() != null) {
            response.body(response.body() + '\n');
            if (response.body().startsWith("(error)")) {
                response.status(400);
            }
        }
    }

    private void formatErrorResponse(Exception e, Request request, Response response) {
        response.status(400);
        response.body(e.getMessage());
    }

    private void internalServerError(Exception e, Request request, Response response) {
        logger.debug("Unexpected error: ", e);
        response.status(500);
        response.body(String.format("(error) Unexpected error: '%s'", e.getMessage()));
    }

    @Bean
    public CommandApi commandApi() {
        return new CommandApi(service(), commandParser);
    }

    @Bean
    public KeyCommandApi keyCommandApi() {
        return new KeyCommandApi(service(), commandParser);
    }

    @Bean
    public StringCommandApi stringCommandApi() {
        return new StringCommandApi(service(), commandParser);
    }

    @Bean
    public SortedSetCommandApi sortedSetCommandApi() {
        return new SortedSetCommandApi(service(), commandParser);
    }
}
