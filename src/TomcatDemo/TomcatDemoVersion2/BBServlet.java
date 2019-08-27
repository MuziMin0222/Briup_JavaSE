package TomcatDemo.TomcatDemoVersion2;

import java.io.InputStream;
import java.io.OutputStream;

public class BBServlet implements Servlet {
    @Override
    public void init() {
        System.out.println("BBServlet....init()");
    }

    @Override
    public void Service(InputStream is, OutputStream os) {
        System.out.println("BBServlet....Service()");
    }

    @Override
    public void destroy() {
        System.out.println("BBServlet....destroy()");
    }
}
