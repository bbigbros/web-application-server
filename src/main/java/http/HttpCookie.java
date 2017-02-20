package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.Map;

/**
 * Created by stripes on 2017. 2. 19..
 */
public class HttpCookie {
    private static final Logger log = LoggerFactory.getLogger(HttpCookie.class);
    private Map<String, String> cookies;

    HttpCookie (String cookieValues) {
         cookies = HttpRequestUtils.parseCookies(cookieValues);
    }

    public String getCookie(String value) {
        return cookies.get(value);
    }
}
