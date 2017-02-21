package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stripes on 2017. 2. 19..
 */
public class HttpSessions {
    private static final Logger log = LoggerFactory.getLogger(HttpSessions.class);
    private static Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();

    public static HttpSession getSession(String id) {
        HttpSession session = sessions.get(id);

        if (session == null) {
            session = new HttpSession(id);
            sessions.put(id, session);
            return session;
        }
        return session;
    }

    static void remove(String id) {
        sessions.remove(id);
    }
}
