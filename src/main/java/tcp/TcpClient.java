package tcp;

import org.apache.commons.lang3.StringUtils;
import utils.IOUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author: pengs.yuan
 * @Date: 19-7-8
 * @Description: Tcp 客户端
 */
public class TcpClient {
    public static void main(String[] args) {
        InetAddress serverHost;

        // 从命令行读入参数
        if (args.length < 1) {
            System.out.println("参数个数不足");
            System.out.println("serverIp [serverPort]");
            return;
        }
        String serverIP = args[0];         // Tcp服务端ip ，输入 null 时 默认是本机 127.0.0.1
        String serverPort = null;        // 默认端口9090

        if (args.length >= 2) {
            serverPort = args[1];
        }

        // Tcp 服务端默认端口
        if (StringUtils.isEmpty(serverPort) || StringUtils.equalsIgnoreCase("null", serverPort)) {
            serverPort = "9090";
        }

        Integer port = 9090;        // TCP 通信默认的端口号
        if (StringUtils.isNotEmpty(serverPort)
                && !StringUtils.equalsIgnoreCase("null", serverPort)
                && serverPort.matches("^[0-9]*$")) {
            port = Integer.valueOf(serverPort);
        }

        BufferedReader reader = null;
        BufferedWriter writer = null;
        Socket sock = null;
        try {
            serverHost = InetAddress.getByName(serverIP);
            sock = new Socket(serverHost, port);

            reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

            String receivedMessage = null;    // 接收的信息

            Scanner cin = new Scanner(System.in);
            String sendMessage = null;        // 发送的信息
            sendMessage = null;
            // 读取输入内容：
            while (StringUtils.isBlank(sendMessage)) {
                System.out.printf("cin>> ");
                sendMessage = cin.nextLine();
            }

            // 发送消息
            IOUtils.sendSocketString(writer, sendMessage);
            System.out.println("[SendMess]: " + sendMessage);

            // 接收消息
            receivedMessage = IOUtils.receiveSocketString(reader);
            System.out.println("[Received]: " + receivedMessage);

            while (StringUtils.isNotEmpty(receivedMessage) && !StringUtils.equalsIgnoreCase("EndConnection", receivedMessage)) {
                // 读取输入内容：
                sendMessage = null;
                while (StringUtils.isBlank(sendMessage)) {
                    System.out.printf("cin>> ");
                    sendMessage = cin.nextLine();
                }
                // 发送消息
                IOUtils.sendSocketString(writer, sendMessage);
                System.out.println("[SendMess]: " + sendMessage);

                // 接收消息
                receivedMessage = IOUtils.receiveSocketString(reader);
                System.out.println("[Received]: " + receivedMessage);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            System.out.println("Close Connection!");
            closeResult(writer, reader, sock);
        }

    }

    /**
     * 关闭输入输出流与socket
     *
     * @param writer
     * @param reader
     * @param sock
     */
    public static void closeResult(BufferedWriter writer, BufferedReader reader, Socket sock) {
        try {
            if (writer != null) writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (sock != null) sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
