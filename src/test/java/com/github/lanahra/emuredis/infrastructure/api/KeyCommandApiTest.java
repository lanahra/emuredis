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
import spark.utils.IOUtils;

public class KeyCommandApiTest {

    @Test
    public void executesDbSizeCommandSuccessfully() throws IOException {
        HttpResponse response =
                Request.Options("http://localhost:8080/dbsize").execute().returnResponse();

        assertThat(response.getStatusLine().getStatusCode(), is(200));
    }

    @Test
    public void executesDeleteCommandSuccessfully() throws IOException {
        Request.Get("http://localhost:8080/?cmd=SET%20mykey%20myvalue").execute().discardContent();

        HttpResponse response =
                Request.Delete("http://localhost:8080/mykey").execute().returnResponse();

        String content = IOUtils.toString(response.getEntity().getContent());

        assertThat(response.getStatusLine().getStatusCode(), is(200));
        assertThat(content, is("(integer) 1\n"));
    }

    @Before
    public void setup() {
        Executor.closeIdleConnections();
    }

    @ClassRule public static final ApplicationContextRule rule = new ApplicationContextRule();
}
