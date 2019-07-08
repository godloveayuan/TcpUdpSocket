package udp;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * @Author: pengs.yuan
 * @Date: 19-7-8
 * @Description:
 */
public class UdpServer {

    private static Integer serverPort = 9191;   // 默认端口9090

    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            // 参数里可以指定端口
            if (args.length > 0) {
                String portStr = args[0];
                if (StringUtils.isNotEmpty(portStr)
                        && !StringUtils.equalsIgnoreCase("null", portStr)
                        && portStr.matches("^[0-9]*$"))
                    serverPort = Integer.valueOf(portStr);
            }

            // 开启一个Socket监听端口
            socket = new DatagramSocket(serverPort);
            System.out.println("UdpServer Listen port:" + serverPort);

            byte[] receivedByte = new byte[1024];
            String receivedMessage = null;
            String sendMessage = null;

            boolean flag = true;
            while (flag) {
                // 接收消息
                DatagramPacket receivedPacket = new DatagramPacket(receivedByte, receivedByte.length);
                socket.receive(receivedPacket);

                // 请求方信息
                InetAddress clientHost = receivedPacket.getAddress();
                String clientIP = clientHost.getHostAddress();
                int clientPort = receivedPacket.getPort();
                receivedMessage = new String(receivedByte, 0, receivedByte.length);
                Arrays.fill(receivedByte, (byte) 0);

                System.out.println("\n[Received]: IP-" + clientIP + " Port-" + clientPort);
                System.out.println("[Received]: " + receivedMessage);

                if (StringUtils.isNotEmpty(receivedMessage) && !StringUtils.equalsIgnoreCase("exit", receivedMessage.trim())) {
                    sendMessage = "Server Received";
                } else {
                    // 响应结束消息
                    sendMessage = "EndConnection";
                }

                // 响应信息
                sendUdpMessage(socket, clientHost, clientPort, sendMessage);
            }

            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    /**
     * 向主机发送udp消息
     *
     * @param socket
     * @param host
     * @param port
     * @param sendMessage
     */
    public static void sendUdpMessage(DatagramSocket socket, InetAddress host, int port, String sendMessage) {
        try {
            DatagramPacket sendPacket = new DatagramPacket(sendMessage.getBytes(), sendMessage.getBytes().length, host, port);
            socket.send(sendPacket);
            System.out.println("[SendMess]: " + sendMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
