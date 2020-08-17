package com.github.lanahra.emuredis.infrastructure.api;

import com.github.lanahra.emuredis.infrastructure.cli.Parser;
import spark.Request;
import spark.Response;
import spark.Service;

public class StringCommandApi {

    private static final String QUERY_PARAM_EXPIRATION = "ex";

    private final Parser parser;

    public StringCommandApi(Service service, Parser parser) {
        this.parser = parser;
        declareEndpoints(service);
    }

    private void declareEndpoints(Service service) {
        service.put("/:key", this::setValue);
        service.get("/:key", this::getValue);
        service.post("/:key/incr", this::incrementValue);
    }

    private String setValue(Request request, Response response) {
        return parser.parse(setCommandFrom(request));
    }

    private String[] setCommandFrom(Request request) {
        String expiration = request.queryParams(QUERY_PARAM_EXPIRATION);
        return expiration == null
                ? new String[] {"set", request.params("key"), bodyFrom(request)}
                : new String[] {"set", request.params("key"), bodyFrom(request), "ex", expiration};
    }

    private String bodyFrom(Request request) {
        String body = request.body();
        return body == null ? "" : body;
    }

    private String getValue(Request request, Response response) {
        return parser.parse(new String[] {"get", request.params("key")});
    }

    private String incrementValue(Request request, Response response) {
        return parser.parse(new String[] {"incr", request.params("key")});
    }
}
