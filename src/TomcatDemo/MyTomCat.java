package TomcatDemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/*
模拟浏览器的工作形式
1、建立一个socket对象，连接网站域名的80端口
2、获取输出流对象
3、获取输入流对象
4、将http协议的请求部分发到服务端
5、读取来自服务端的数据打印在控制台
6、关闭流资源
 */
public class MyTomCat {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            socket= new Socket("www.baidu.com", 80);
            os = socket.getOutputStream();
//            BufferedOutputStream bos = new BufferedOutputStream(os);
            is= socket.getInputStream();
//            BufferedInputStream bis = new BufferedInputStream(is);

            os.write(("GET /index.html HTTP/1.1" + "\r\n").getBytes());
            os.write(("Host: www.baidu.com" + "\r\n").getBytes());
            os.write("\r\n\r\n".getBytes());
            os.flush();

            int len = is.read();
            while (len != -1){
                System.out.print((char)len);
                len = is.read();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            is.close();
            os.close();
            socket.close();
        }
    }
}
