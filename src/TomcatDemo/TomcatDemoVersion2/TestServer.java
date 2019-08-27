package TomcatDemo.TomcatDemoVersion2;

import java.io.*;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TestServer {
    //定义一个变量，存放服务器WebContent目录的绝对路径
    public static String WEB_ROOT = System.getProperty("user.dir") + "\\" + "WebContent";
    //定义一个静态变量，用于存放本次请求的静态页面名称
    private static String url = "";
    //定义一个静态的MAP,存储服务端conf.properties中的配置信息
    private static Map<String,String> map = new HashMap<>();

    //为了让服务器启动之前将配置信息中的参数加载在Map中
    static {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(WEB_ROOT + "\\conf.properties"));
            Set<Object> set= prop.keySet();
            Iterator<Object> it = set.iterator();
            while (it.hasNext()){
                String key = (String)it.next();
                String value = prop.getProperty(key);
                map.put(key,value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(map);
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

                //判断本次请求的是静态demo.html页面还是运行在服务器端一段java小程序
                if (url != null){
                    if (url.indexOf(".") != -1){
                        //发送静态资源
                        sendStaticResource(os);
                    }else {
                        sendDynamicResource(os,is);
                    }
                }
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

    private static void sendDynamicResource(OutputStream os, InputStream is) {
        try {
            //将http协议的响应行和响应头发送到客户端
            os.write("HTTP/1.1 200 OK\r\n".getBytes());
            os.write("Content-Type:text/html\r\n".getBytes());
            os.write("\r\n".getBytes());
            //判断map中是否存在一个key，这个key是否和本次待请求的资源路径一致
            if (map.containsKey(url)){
                //如果包含指定的key，获取到Map中的key对应的value部分
                String value = map.get(url);
                //通过反射将对应的java程序加载到内存
                Class ac = Class.forName(value);
                Constructor cons = ac.getDeclaredConstructor();
                Servlet servlet = (Servlet)cons.newInstance();
                //执行init方法
                servlet.init();
                //执行service方法
                servlet.Service(is, os);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
