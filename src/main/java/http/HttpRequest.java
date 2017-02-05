package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.RequestHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stripes on 2017. 1. 28..
 */
public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private Map<String, String> requestLine = new HashMap<String, String>();
    private Map<String, String> requestHeaders = new HashMap<String, String>();
    private Map<String, String> requestPathParams = new HashMap<String, String>();
    private int contentLength;

    /*
     * HttpRequest 생성자 생성
     */
    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String requestLine = br.readLine();

            if (requestLine == null) {
                return;
            }
            processHttpRequest(requestLine, br);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processHttpRequest(String line, BufferedReader br) throws IOException {
        String[] tokens = line.split(" ");
        requestLine.put("method", tokens[0]);
        requestLine.put("path", tokens[1]);
        requestLine.put("httpVer", tokens[2]);

        while(!line.equals("")) {
            line = br.readLine();
            if (line == null || line.equals("")) break;
            tokens = line.split(":");
            requestHeaders.put(tokens[0], tokens[1].trim());
        }
        setPathParameter(requestLine.get("path"));

        if (contentLength > 0) {
            String data = IOUtils.readData(br, contentLength);
            requestPathParams = HttpRequestUtils.parseQueryString(data);
        }

    }

    private void setPathParameter(String path) {
        if (isPost()) {
            contentLength = Integer.parseInt(requestHeaders.get("Content-Length"));
        } else {
            String pathInfo = HttpRequestUtils.getMethodCleanData(path);
            requestPathParams = HttpRequestUtils.parseQueryString(pathInfo);
        }
    }

    public boolean isPost() {
        if (requestLine.get("method").equals("POST")) {
            return true;
        }
        return false;
    }

    public String getParameter(String key) {
        return requestPathParams.get(key);
    }

    public String getHeader(String key) {
        return requestHeaders.get(key);
    }

    public String getMethod() { return requestLine.get("method"); }

    public String getPath() { return requestLine.get("path"); }

    public String getHttpVersion() {
        return requestLine.get("httpVer");
    }
}
