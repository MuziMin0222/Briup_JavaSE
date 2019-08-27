package TomcatDemo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TestServer01 {
    //定义一个变量，存放服务器WebContent目录的绝对路径
    public static String WEB_ROOT = System.getProperty("user.dir") + "\\" + "WebContent";
    //定义一个静态变量，用于存放本次请求的静态页面名称
    private static String url = "";

    public static void main(String[] args) {
        ServerSocket serverSocket =null;
        Socket socket =null;
        OutputStream os = null;
        InputStream is = null;
        try {
            serverSocket= new ServerSocket(9876);
            while (true){
                socket = serverSocket.accept();

                os = socket.getOutputStream();
                is = socket.getInputStream();

                //获取http协议的请求部分，截取客户端要访问的资源名称，将这个资源名称赋值给URL
                parse(is);

                //发送静态资源
                sendStaticResource(os);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                os.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    //发送静态资源
    private static void sendStaticResource(OutputStream os) {
        //定义一个字节数组，用于存放本次请求的静态资源demo1.html的内容
        byte[] bys = new byte[1024];
        //定义一个文件输入流，用户获取静态资源demo1.HTML中的内容
        File file = new File(WEB_ROOT, url);
        FileInputStream fis = null;
        //如果文件存在
        try{
            if (file.exists()){
                //向客户端输出http协议的响应行/响应头
                os.write("HTTP/1.1 200 OK\r\n".getBytes());
                os.write("Content-Type:text/html;charset=UTF-8\r\n".getBytes());
                os.write("\r\n".getBytes());
                //获取到文件输入流对象
                fis = new FileInputStream(file);
                //读取静态资源内容到数组中
                //将读取的数组中的内容通过输出流发送到客户端
                int len = 0;
                while ((len = fis.read(bys)) != -1){
                    os.write(new String(bys,0,len, StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8));
                }
            }else {
                //如果文件不存在
                //向客户端响应文件不存在的信息
                os.write("HTTP/1.1 404 not found\r\n".getBytes());
                os.write("Content-Type:text/html;charset=utf-8\r\n".getBytes());
                os.write("\r\n".getBytes());
                String errorMessage = "file not fount";
                os.write(errorMessage.getBytes());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //获取http协议的请求部分，截取客户端要访问的资源名称，将这个资源名称赋值给URL
    private static void parse(InputStream is) throws IOException {
//        byte[] bys = new byte[1024];
//        int len = 0;
//        while ((len = is.read(bys)) != -1){
//            System.out.println(new String(bys,0,len));
//        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = br.readLine();
        System.out.println(line);

        String[] s = line.split(" ");
        String s1 = s[1].substring(1);
//        String s2 = s1.substring(1);
//        System.out.println(s2);
        url = s1;
    }
}
