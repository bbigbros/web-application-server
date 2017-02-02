package controller;

import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

/**
 * Created by stripes on 2017. 2. 2..
 */
public class AbstractController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public void service(HttpRequest request, HttpResponse response) {
        log.debug("AbstractController Running...");

        if (request.getMethod().equals("POST")) {
            doPost(request, response);
        } else {
            doGet(request, response);
        }

    }
    void doPost(HttpRequest request, HttpResponse response) { }
    void doGet(HttpRequest request, HttpResponse response) {
        log.debug(" abstract 유레카!!!!!!!!!!!!!");
    }
}
