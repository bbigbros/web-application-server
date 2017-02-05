package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by stripes on 2017. 1. 30..
 */
public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private DataOutputStream dos = null;
    private Map<String , String> responseHeaders = new HashMap<String, String>();

    public HttpResponse(OutputStream out) {
        dos = new DataOutputStream(out);
    }

    public void forward(String path) {
        byte[] body = null;
        try {
            body = Files.readAllBytes(new File("./webapp" + path).toPath());

            if (path.endsWith(".css")) {
                responseHeaders.put("Content-Type", "text/css;charset=utf-8");
            } else if(path.endsWith(".js")) {
                responseHeaders.put("Content-Type", "text/javascript;charset=utf-8");
            } else {
                responseHeaders.put("Content-Type", "text/html;charset=utf-8");
            }
            responseHeaders.put("Content-Length", String.valueOf(body.length));
        }catch (Exception e){
            e.getMessage();
        }
        response200Header(body.length);
        responseBody(body);
    }

    public void sendRedirect(String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            addHeadersInfo();
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void forwardBody(String body) {
        byte[] contents = body.getBytes();
        responseHeaders.put("Content-Type", "text/html;charset=utf-8");
        responseHeaders.put("Content-Length", contents.length + "");
        response200Header(contents.length);
        responseBody(contents);
    }

    public void addHeader(String key, String value) {
        responseHeaders.put(key, value);
    }

    private void addHeadersInfo() {
        Map<String, String> m = responseHeaders;
        for(String key : m.keySet()) {
            try {
                dos.writeBytes(key + ": " + m.get(key) + "\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void response200Header(int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            addHeadersInfo();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
