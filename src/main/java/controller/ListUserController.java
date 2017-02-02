package controller;

import http.HttpRequest;
import http.HttpResponse;

/**
 * Created by stripes on 2017. 2. 2..
 */
public class ListUserController extends AbstractController {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {

    }

    public boolean isLogin(String a) {
        return true;
    }
}
