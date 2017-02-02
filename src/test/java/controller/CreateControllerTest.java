package controller;


import http.HttpRequest;
import http.HttpRequestTest;
import http.HttpResponse;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stripes on 2017. 2. 2..
 */
public class CreateControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(CreateControllerTest.class);
    String path = "./src/test/resources/";

    @Test
    public void createController() throws Exception {
        InputStream in = new FileInputStream(new File("./src/test/resources/http_POST.txt"));
        HttpResponse response = new HttpResponse(createOutputStream("Http_Redirect.txt"));
        HttpRequest request = new HttpRequest(in);

        Map<String, Controller> controllers = new HashMap<String, Controller>();
        controllers.put("/user/create", new CreateUserController());
        controllers.put("/user/login", new LoginController());
        controllers.put("/list", new ListUserController());

        logger.debug("map {}", controllers);
        Controller c = controllers.get(request.getPath());

        logger.debug("{}", c);
        c.service(request, response);
    }

    private OutputStream createOutputStream(String filename) throws FileNotFoundException{
        return new FileOutputStream(new File(path + filename));
    }
}
