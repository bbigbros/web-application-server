package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpSession;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;

/**
 * Created by stripes on 2017. 2. 2..
 */
public class LoginController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        try {
            log.debug("loginController cookie chk : {}", (request.getCookies().getCookie("JSESSIONID")));
            User user = DataBase.findUserById(request.getParameter("userId"));
            if(user != null) {
                if (user.getPassword().equals(request.getParameter("password"))){
                    log.debug("http session chk : {}", (request.getCookies().getCookie("JSESSIONID")));
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    response.sendRedirect("/index.html");
                } else {
                    response.forward("/user/login_failed.html");
                }
            }

        } catch (NullPointerException e) {
            log.debug("id가 존재하지 않습니다.");
            e.getMessage();
        }
    }

}
