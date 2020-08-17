package com.github.lanahra.emuredis.infrastructure.api;

public class QueryParamRequiredException extends RuntimeException {

    public QueryParamRequiredException(String queryParam) {
        super(String.format("(error) ERR Query param is required: '%s'", queryParam));
    }
}
