package com.github.lanahra.emuredis.infrastructure.api;

import com.github.lanahra.emuredis.infrastructure.cli.LineParser;
import com.github.lanahra.emuredis.infrastructure.cli.Parser;
import spark.Request;
import spark.Response;
import spark.Service;

public class CommandApi {

    private static final String QUERY_PARAM_COMMAND = "cmd";

    private final Parser parser;

    public CommandApi(Service service, Parser parser) {
        declareEndpoints(service);
        this.parser = parser;
    }

    private void declareEndpoints(Service service) {
        service.get("/", this::parseCommand);
    }

    private String parseCommand(Request request, Response response) {
        String[] args = argsFrom(request);
        return parser.parse(args);
    }

    private String[] argsFrom(Request request) {
        String command = commandParamFrom(request);
        return LineParser.parseLine(command);
    }

    private String commandParamFrom(Request request) {
        String param = request.queryParams(QUERY_PARAM_COMMAND);
        if (param == null) {
            throw new QueryParamRequiredException(QUERY_PARAM_COMMAND);
        }
        return param;
    }
}
