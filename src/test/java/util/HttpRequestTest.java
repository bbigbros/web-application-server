package util;

import http.HttpRequest;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by stripes on 2017. 1. 28..
 */
public class HttpRequestTest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestTest.class);

    @Test
    public void request_get() throws IOException {
        InputStream in = new FileInputStream(new File("./src/test/resources/http_GET.txt"));
        HttpRequest hr = new HttpRequest(in);

        assertEquals("javajigi", hr.getParameter("userId"));
        assertEquals("password", hr.getParameter("password"));
        assertEquals("JaeSung", hr.getParameter("name"));
        assertEquals("GET", hr.getMethod());
        assertEquals("keep-alive", hr.getHeader("Connection"));
    }
}
