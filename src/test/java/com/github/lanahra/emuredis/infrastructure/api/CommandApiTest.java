package com.github.lanahra.emuredis.infrastructure.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.lanahra.emuredis.infrastructure.spring.ApplicationContextRule;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

public class CommandApiTest {

    @Test
    public void parsesCommandFromQueryStringAndReturnsSuccessfulResponse() throws IOException {
        HttpResponse response =
                Request.Get("http://localhost:8080/?cmd=SET%20mykeya%20myvalue")
                        .execute()
                        .returnResponse();

        assertThat(response.getStatusLine().getStatusCode(), is(200));
    }

    @Test
    public void parsesCommandFromQueryStringAndReturnsBadRequestForEvalError() throws IOException {
        HttpResponse response =
                Request.Get("http://localhost:8080/?cmd=SET%20mykeyb%20myvalue%20EX%20noninteger")
                        .execute()
                        .returnResponse();

        assertThat(response.getStatusLine().getStatusCode(), is(400));
    }

    @Test
    public void requestWithoutCommandQueryParamReturnsBadRequest() throws IOException {
        HttpResponse response = Request.Get("http://localhost:8080/").execute().returnResponse();

        assertThat(response.getStatusLine().getStatusCode(), is(400));
    }

    @Before
    public void setup() {
        Executor.closeIdleConnections();
    }

    @ClassRule public static final ApplicationContextRule rule = new ApplicationContextRule();
}
