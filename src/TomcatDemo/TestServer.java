package TomcatDemo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
模拟服务器端
1、创建serverSocket对象，监听端口号是8080端口号
2、等待来自客户端的请求获取和客户端对应socket对象
3、通过获取到的socket对象获取到输出流对象
4、通过获取到的输出流对象将http协议的响应部分发送到客户端
5、发送资源
 */
public class TestServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket socket = null;
        OutputStream os = null;
        try{
            serverSocket = new ServerSocket(9999);
            System.out.println("服务器启动成功。。。。");
            while (true){
                socket = serverSocket.accept();
                System.out.println("连接成功");
                os = socket.getOutputStream();
                os.write(("HTTP/1.1 200 OK" + "\r\n").getBytes());
                os.write(("Content-Type:text/html" + "\r\n").getBytes());
                os.write("\r\n\r\n".getBytes());
                os.flush();
                System.out.println("========");

                StringBuffer sb = new StringBuffer();
                sb.append("<html>");
                sb.append("<head>");
                sb.append("<title>this is a html</title>");
                sb.append("</head>");
                sb.append("<body>");
                sb.append("<h1>this is a html</h1>");
                sb.append("</body>");
                sb.append("</html>");
                System.out.println("------");
                String s = sb.toString();
                os.write(s.getBytes());
                System.out.println("+++++++");
                os.flush();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            serverSocket.close();
            socket.close();
            os.close();
        }
    }
}
