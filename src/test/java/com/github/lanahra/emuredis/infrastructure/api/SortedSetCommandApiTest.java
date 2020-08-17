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

public class SortedSetCommandApiTest {

    @Test
    public void executesZAddCommandSuccessfully() throws IOException {
        HttpResponse response =
                Request.Put("http://localhost:8080/mykeya/zadd/mymember?score=1.0")
                        .execute()
                        .returnResponse();

        String content = IOUtils.toString(response.getEntity().getContent());

        assertThat(response.getStatusLine().getStatusCode(), is(200));
        assertThat(content, is("(integer) 1\n"));
    }

    @Test
    public void executesZCardCommandSuccessfully() throws IOException {
        HttpResponse response =
                Request.Get("http://localhost:8080/mykeyb/zcard").execute().returnResponse();

        String content = IOUtils.toString(response.getEntity().getContent());

        assertThat(response.getStatusLine().getStatusCode(), is(200));
        assertThat(content, is("(integer) 0\n"));
    }

    @Test
    public void executesZRankCommandSuccessfully() throws IOException {
        Request.Put("http://localhost:8080/mykeyc/zadd/mymember?score=1.0")
                .execute()
                .discardContent();

        HttpResponse response =
                Request.Get("http://localhost:8080/mykeyc/zrank/mymember")
                        .execute()
                        .returnResponse();

        String content = IOUtils.toString(response.getEntity().getContent());

        assertThat(response.getStatusLine().getStatusCode(), is(200));
        assertThat(content, is("(integer) 0\n"));
    }

    @Test
    public void executesZRangeCommandSuccessfully() throws IOException {
        Request.Put("http://localhost:8080/mykeyd/zadd/mymember?score=1.0")
                .execute()
                .discardContent();

        HttpResponse response =
                Request.Get("http://localhost:8080/mykeyd/zrange?start=0&stop=-1")
                        .execute()
                        .returnResponse();

        String content = IOUtils.toString(response.getEntity().getContent());

        assertThat(response.getStatusLine().getStatusCode(), is(200));
        assertThat(content, is("1) \"mymember\"\n"));
    }

    @Test
    public void zRangeCommandWithoutStartQueryParamReturnsBadRequest() throws IOException {
        Request.Put("http://localhost:8080/mykeye/zadd/mymember?score=1.0")
                .execute()
                .discardContent();

        HttpResponse response =
                Request.Get("http://localhost:8080/mykeye/zrange?stop=-1")
                        .execute()
                        .returnResponse();

        assertThat(response.getStatusLine().getStatusCode(), is(400));
    }

    @Test
    public void zRangeCommandWithoutStopQueryParamReturnsBadRequest() throws IOException {
        Request.Put("http://localhost:8080/mykeyf/zadd/mymember?score=1.0")
                .execute()
                .discardContent();

        HttpResponse response =
                Request.Get("http://localhost:8080/mykeyf/zrange?start=0")
                        .execute()
                        .returnResponse();

        assertThat(response.getStatusLine().getStatusCode(), is(400));
    }

    @Before
    public void setup() {
        Executor.closeIdleConnections();
    }

    @ClassRule public static final ApplicationContextRule rule = new ApplicationContextRule();
}
