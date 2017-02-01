package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        // RequestHandler는 사용자의 요청이 있을 때까지 대기 상태에 있는다.
        // 요청이 있을 경우 RequestHandler 클래스에 위임하는 역할을 함.
        System.out.println("Start RequestHandler");

        // 서버 입장에서 입력 : InputStream
        // 서버 입장에서 출력 : OutputStream
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            if (request.getMethod().equals("GET")) {
                if(request.getPath().startsWith("/user/create")) {
                    response.sendRedirect("/index.html");
                } else if (request.getPath().startsWith("/user/list.html")) {
//                    createListUser(dos, HttpRequestUtils.parseCookies(cookies));
                    log.debug("list");
                } else {
                    log.debug("get default");
                    response.forward(request.getPath());
                }
            } else if (request.getMethod().equals("POST")) {
                if(request.getPath().startsWith("/user/create")) {
                    response.sendRedirect("/index.html");
                } else if (request.getPath().startsWith("/user/login")) {
                    log.debug("post login");
                    response.sendRedirect("/index.html");
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
