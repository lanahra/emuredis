package com.github.lanahra.emuredis.infrastructure.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.lanahra.emuredis.infrastructure.spring.ApplicationContextRule;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import spark.utils.IOUtils;

public class StringCommandApiTest {

    @Test
    public void executesSetCommandSuccessfully() throws IOException {
        HttpResponse response =
                Request.Put("http://localhost:8080/mykeya")
                        .bodyString("myvalue", ContentType.TEXT_PLAIN)
                        .execute()
                        .returnResponse();

        String content = IOUtils.toString(response.getEntity().getContent());

        assertThat(response.getStatusLine().getStatusCode(), is(200));
        assertThat(content, is("OK\n"));
    }

    @Test
    public void executesSetCommandWithExpirationSuccessfully() throws IOException {
        HttpResponse response =
                Request.Put("http://localhost:8080/mykeyb?ex=10")
                        .bodyString("myvalue", ContentType.TEXT_PLAIN)
                        .execute()
                        .returnResponse();

        String content = IOUtils.toString(response.getEntity().getContent());

        assertThat(response.getStatusLine().getStatusCode(), is(200));
        assertThat(content, is("OK\n"));
    }

    @Test
    public void executesSetCommandWithEmptyValueSuccessfully() throws IOException {
        HttpResponse response =
                Request.Put("http://localhost:8080/mykeyc")
                        .bodyString("", ContentType.TEXT_PLAIN)
                        .execute()
                        .returnResponse();

        String content = IOUtils.toString(response.getEntity().getContent());

        assertThat(response.getStatusLine().getStatusCode(), is(200));
        assertThat(content, is("OK\n"));
    }

    @Test
    public void executesSetCommandWithNoBodySuccessfully() throws IOException {
        HttpResponse response =
                Request.Put("http://localhost:8080/mykeyd").execute().returnResponse();

        String content = IOUtils.toString(response.getEntity().getContent());

        assertThat(response.getStatusLine().getStatusCode(), is(200));
        assertThat(content, is("OK\n"));
    }

    @Test
    public void setCommandWithNonIntegerExpirationReturnsBadRequest() throws IOException {
        HttpResponse response =
                Request.Put("http://localhost:8080/mykeye?ex=noninteger")
                        .bodyString("myvalue", ContentType.TEXT_PLAIN)
                        .execute()
                        .returnResponse();

        assertThat(response.getStatusLine().getStatusCode(), is(400));
    }

    @Test
    public void executesGetCommandSuccessfully() throws IOException {
        Request.Get("http://localhost:8080/?cmd=SET%20mykeyf%20myvalue").execute().discardContent();

        HttpResponse response =
                Request.Get("http://localhost:8080/mykeyf").execute().returnResponse();

        String content = IOUtils.toString(response.getEntity().getContent());

        assertThat(response.getStatusLine().getStatusCode(), is(200));
        assertThat(content, is("\"myvalue\"\n"));
    }

    @Test
    public void executesIncrCommandSuccessfully() throws IOException {
        HttpResponse response =
                Request.Post("http://localhost:8080/mykeyg/incr").execute().returnResponse();

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
