package com.github.lanahra.emuredis.infrastructure.api;

import com.github.lanahra.emuredis.infrastructure.cli.Parser;
import spark.Request;
import spark.Response;
import spark.Service;

public class SortedSetCommandApi {

    private static final String QUERY_PARAM_SCORE = "score";
    private static final String QUERY_PARAM_START = "start";
    private static final String QUERY_PARAM_STOP = "stop";

    private final Parser parser;

    public SortedSetCommandApi(Service service, Parser parser) {
        this.parser = parser;
        declareEndpoints(service);
    }

    private void declareEndpoints(Service service) {
        service.put("/:key/zadd/:member", this::addMember);
        service.get("/:key/zcard", this::cardinalityOfSortedSet);
        service.get("/:key/zrank/:member", this::rankOfMemberInSortedSet);
        service.get("/:key/zrange", this::rangeFromSortedSet);
    }

    private String addMember(Request request, Response response) {
        String score = requiredParamFrom(request, QUERY_PARAM_SCORE);
        return parser.parse(
                new String[] {"zadd", request.params("key"), score, request.params("member")});
    }

    private String cardinalityOfSortedSet(Request request, Response response) {
        return parser.parse(new String[] {"zcard", request.params("key")});
    }

    private String rankOfMemberInSortedSet(Request request, Response response) {
        return parser.parse(
                new String[] {"zrank", request.params("key"), request.params("member")});
    }

    private String rangeFromSortedSet(Request request, Response response) {
        String start = requiredParamFrom(request, QUERY_PARAM_START);
        String stop = requiredParamFrom(request, QUERY_PARAM_STOP);
        return parser.parse(new String[] {"zrange", request.params("key"), start, stop});
    }

    private String requiredParamFrom(Request request, String queryParam) {
        String param = request.queryParams(queryParam);
        if (param == null) {
            throw new QueryParamRequiredException(queryParam);
        }
        return param;
    }
}
