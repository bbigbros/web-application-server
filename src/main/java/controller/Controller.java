package controller;

import http.HttpRequest;
import http.HttpResponse;

/**
 * Created by stripes on 2017. 2. 2..
 */
public interface Controller {
    void service(HttpRequest request, HttpResponse response);
}
