package TomcatDemo.TomcatDemoVersion2;

import java.io.InputStream;
import java.io.OutputStream;

//这是所有java小程序要实现的接口
public interface Servlet {
    //初始化
    void init();
    //服务
    void Service(InputStream is, OutputStream os);
    //销毁
    void destroy();
}
