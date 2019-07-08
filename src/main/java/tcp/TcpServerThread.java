package tcp;

import org.apache.commons.lang3.StringUtils;
import utils.IOUtils;

import java.io.*;
import java.net.Socket;

/**
 * @Author: pengs.yuan
 * @Date: 19-7-8
 * @Description:
 */
public class TcpServerThread extends Thread {
    private Socket sock = null;

    public TcpServerThread(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        try {
            // 输入流：读取请求消息
            BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            // 输出流：写入响应消息
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

            // 获取客户端的IP地址和发送端口
            String clientIP = sock.getInetAddress().getHostAddress();
            int clientPort = sock.getPort();
            System.out.println("*********************************");
            System.out.println("* clientIP   = " + clientIP + "\t*");
            System.out.println("* clientPort = " + clientPort + "\t\t*");
            System.out.println("*********************************");

            String requestMessage = null;     // 接收到的客户端请求消息
            String responseMessage = null;    // 回复给客户端的响应消息

            // 接收消息
            requestMessage = IOUtils.receiveSocketString(reader);
            System.out.println("[Received]: " + requestMessage);

            while (!StringUtils.equalsIgnoreCase("exit", requestMessage)) {
                // 响应消息
                responseMessage = "Server Received";
                IOUtils.sendSocketString(writer, responseMessage);
                System.out.println("[SendMess]: " + responseMessage);

                requestMessage = IOUtils.receiveSocketString(reader);
                System.out.println("[Received]: " + requestMessage);
            }

            // 响应消息
            responseMessage = "EndConnection";
            IOUtils.sendSocketString(writer, responseMessage);
            System.out.println("[SendMess]: " + responseMessage);

            // 关闭资源
            System.out.println("Connection close!");
            writer.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
