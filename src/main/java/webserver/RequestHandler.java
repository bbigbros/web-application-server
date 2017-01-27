package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import db.DataBase;
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

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            DataOutputStream dos = new DataOutputStream(out);
            String line = br.readLine();

            // 만약 null일 경우 더 진행할 의미가 없다.
            if(line == null) return;

            log.debug("header: {}", line);
            String[] tokens = line.split(" ");
            String method = tokens[0];
            String requestUrl = tokens[1];
            String cookies = "";
            int length = 0;

            while(!line.equals("")) {
                line = br.readLine();
                log.debug("header: {}", line);
                if(line.contains("Content-Length")){
                    int index = line.indexOf(" ");
                    length = Integer.parseInt(line.substring(index + 1));
                } else if (line.contains("Cookie")) {
                    log.debug("cookie: {}", line);
                    cookies = line;
                }
            }

            if (method.toLowerCase().equals("get")) {
                if(requestUrl.startsWith("/user/create")) {
                    requestUrl = HttpRequestUtils.getMethodCleanData(requestUrl);
                    Map<String, String>map = HttpRequestUtils.parseQueryString(requestUrl);
                    createUser(dos, map);
                } else if (requestUrl.startsWith("/user/list.html")) {
                    createListUser(dos, HttpRequestUtils.parseCookies(cookies));
                } else if (requestUrl.endsWith(".css")) {
                    defaultResponse(dos, readFirstUrl(requestUrl), "text/css");
                } else {
                    defaultResponse(dos, readFirstUrl(requestUrl), "text/html");
                }
            } else if (method.toLowerCase().equals("post")) {
                if (requestUrl.startsWith("/user/create")) {
                    createUser(dos, getParams(br, length));
                } else if (requestUrl.startsWith("/user/login")) {
                    Map<String, String> map = getParams(br, length);
                    User u = DataBase.findUserById(map.get("userId"));
                    if (u.getPassword().equals(map.get("password"))) {
                        // login success
                        response302Header(dos, "/index.html", "logined=true");
                    } else {
                        // login failed
                        response302Header(dos, "/user/login_failed.html", "logined=false");
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void defaultResponse(DataOutputStream dos, byte[] body, String type) {
        response200Header(dos, body.length, type);
        responseBody(dos, body);
    }

    private void createListUser(DataOutputStream dos, Map<String, String>map) {
        if (Boolean.parseBoolean(map.get("logined"))) {
            User user;
            StringBuilder sb = new StringBuilder();
            Collection<User> users = DataBase.findAll();
            Iterator<User> it = users.iterator();
            sb.append("<table>");
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
            defaultResponse(dos, sb.toString().getBytes(), "text/html");
        }
    }

    private void createUser(DataOutputStream dos, Map<String, String> map) {
        log.debug("Create User check :: {} ", map);
        User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
        DataBase.addUser(user);
        response302Header(dos, "/index.html", "Create User Success!!");
    }

    private Map<String, String> getParams(BufferedReader br, int length) throws IOException {
        String data = IOUtils.readData(br, length);
        Map<String, String> params = HttpRequestUtils.parseQueryString(data);

        return params;
    }

    private byte[] readFirstUrl(String url) {
        byte[] body = null;
        try {
            body = Files.readAllBytes(new File("./webapp" + url).toPath());
        }catch (Exception e){
            e.getMessage();
        }

        return body;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + type + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            if (cookie.startsWith("logined")) {
                dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
            }
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("\r\n");
            log.debug("{}", cookie);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
