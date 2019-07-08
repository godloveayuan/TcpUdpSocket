package udp;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @Author: pengs.yuan
 * @Date: 19-7-8
 * @Description:
 */
public class UdpClient {

    public static void main(String[] args) {

        InetAddress serverHost;    // Server 主机

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

        // UDP 服务端默认端口
        if (StringUtils.isEmpty(serverPort) || StringUtils.equalsIgnoreCase("null", serverPort)) {
            serverPort = "9191";
        }

        Integer port = 9191;        // TCP 通信默认的端口号
        if (StringUtils.isNotEmpty(serverPort)
                && !StringUtils.equalsIgnoreCase("null", serverPort)
                && serverPort.matches("^[0-9]*$")) {
            port = Integer.valueOf(serverPort);
        }

        DatagramPacket sendPacket = null;
        DatagramPacket receivePacket = null;
        String sendMessage = null;
        String receivedMessage = null;
        byte[] receivedBuf = null;

        try {
            serverHost = InetAddress.getByName(serverIP);
            DatagramSocket socket = new DatagramSocket();

            Scanner cin = new Scanner(System.in);
            sendMessage = null;
            // 读取输入内容：
            while (StringUtils.isBlank(sendMessage)) {
                System.out.printf("cin>> ");
                sendMessage = cin.nextLine();
            }

            // 发送消息
            System.out.println("[SendMess]: " + sendMessage);
            sendPacket = new DatagramPacket(sendMessage.getBytes(), sendMessage.getBytes().length, serverHost, port);
            socket.send(sendPacket);

            // 接收消息
            receivedBuf = new byte[1024];
            receivePacket = new DatagramPacket(receivedBuf, receivedBuf.length);
            socket.receive(receivePacket);
            receivedMessage = new String(receivedBuf, 0, receivedBuf.length);
            Arrays.fill(receivedBuf, (byte) 0);
            System.out.println("[Received]: " + receivedMessage);

            while (StringUtils.isNotEmpty(receivedMessage) && !StringUtils.equalsIgnoreCase("EndConnection", receivedMessage.trim())) {
                // 读取输入内容：
                sendMessage = null;
                while (StringUtils.isBlank(sendMessage)) {
                    System.out.printf("cin>> ");
                    sendMessage = cin.nextLine();
                }

                // 发送消息
                System.out.println("[SendMess]: " + sendMessage);
                sendPacket = new DatagramPacket(sendMessage.getBytes(), sendMessage.getBytes().length, serverHost, port);
                socket.send(sendPacket);

                // 接收消息
                receivedBuf = new byte[1024];
                receivePacket = new DatagramPacket(receivedBuf, receivedBuf.length);
                socket.receive(receivePacket);
                receivedMessage = new String(receivedBuf, 0, receivedBuf.length);
                Arrays.fill(receivedBuf, (byte) 0);
                System.out.println("[Received]: " + receivedMessage);
            }

            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
