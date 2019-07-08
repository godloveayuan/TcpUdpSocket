package tcp;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: pengs.yuan
 * @Date: 19-7-8
 * @Description: Tcp服务端
 */
public class TcpServer {
    private static Integer serverPort = 9090;   // 默认端口9090

    public static void main(String[] args) {
        try {

            // 参数里可以指定端口
            if (args.length > 0) {
                String portStr = args[0];
                if (StringUtils.isNotEmpty(portStr)
                        && !StringUtils.equalsIgnoreCase("null", portStr)
                        && portStr.matches("^[0-9]*$"))
                    serverPort = Integer.valueOf(portStr);
            }

            // 服务端开启一个ServerSocket即可
            ServerSocket server = new ServerSocket(serverPort);
            boolean flag = true;
            System.out.println("TcpServer Listen port:" + serverPort);
            while (flag) {
                // 为每一个客户端建立一个socket
                Socket listen = server.accept();
                // 开启线程
                new TcpServerThread(listen).start();
            }

            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
