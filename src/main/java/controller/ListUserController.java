package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpSession;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by stripes on 2017. 2. 2..
 */
public class ListUserController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(ListUserController.class);

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        if(!isLogined(request.getSession())) {
            response.sendRedirect("/");
        }
        User user;
        StringBuilder sb = new StringBuilder();
        Collection<User> users = DataBase.findAll();
        Iterator<User> it = users.iterator();
        sb.append("<table border='1'>");
        while(it.hasNext()) {
            user = it.next();
            sb.append("<tr>");
            sb.append("<td>" + user.getUserId() + "</td>");
            sb.append("<td>" + user.getPassword() + "</td>");
            sb.append("<td>" + user.getName() + "</td>");
            sb.append("<td>" + user.getEmail() + "</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        response.forwardBody(sb.toString());
    }

    public boolean isLogin(String cookies) {
        Map<String, String> cookieValues = HttpRequestUtils.parseCookies(cookies);
        String cookieValue = cookieValues.get("logined");
        if (cookieValue == null) {
            return false;
        }
        return Boolean.parseBoolean(cookieValue);
    }

    private static boolean isLogined(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return false;
        }
        return true;
    }
}
