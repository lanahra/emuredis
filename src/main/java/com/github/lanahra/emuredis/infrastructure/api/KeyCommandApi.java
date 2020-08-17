package com.github.lanahra.emuredis.infrastructure.api;

import com.github.lanahra.emuredis.infrastructure.cli.Parser;
import spark.Request;
import spark.Response;
import spark.Service;

public class KeyCommandApi {

    private final Parser parser;

    public KeyCommandApi(Service service, Parser parser) {
        this.parser = parser;
        declareEndpoints(service);
    }

    private void declareEndpoints(Service service) {
        service.options("/dbsize", this::dbSize);
        service.delete("/:key", this::deleteKey);
    }

    private String dbSize(Request request, Response response) {
        return parser.parse(new String[] {"dbsize"});
    }

    private String deleteKey(Request request, Response response) {
        return parser.parse(new String[] {"del", request.params("key")});
    }
}
