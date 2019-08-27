package TomcatDemo.TomcatDemoVersion2;

import java.io.InputStream;
import java.io.OutputStream;

public class AAServlet implements Servlet {
    @Override
    public void init() {
        System.out.println("AAServlet....init()");
    }

    @Override
    public void Service(InputStream is, OutputStream os) {
        System.out.println("AAServlet....Service()");
    }

    @Override
    public void destroy() {
        System.out.println("AAServlet....destroy()");
    }
}
