package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by stripes on 2017. 2. 19..
 */
public class HttpSession {
    private static final Logger log = LoggerFactory.getLogger(HttpSession.class);
    private Map<String, Object> status = new HashMap<String, Object>();
    private String id;

    HttpSession (String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setAttribute(String name, Object value) {
        status.put(name, value);
    }

    public Object getAttribute(String name) {
        return status.get(name);
    }

    public void removeAttribute(String name) {
        status.remove(name);
    }

    public void invalidate() {
        status.clear();
    }
}
