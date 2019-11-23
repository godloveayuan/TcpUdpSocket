# 操作说明
启动： java -jar *.jar serverIP [serverPort]

# 使用UDP客户端点灯
1. 启动连接
java -jar UdpClient.jar 192.168.1.106 4567

2. 搜索设备
0D wm 123456 searchall

3. 点灯
0A wm 123456 18-fe-34-a4-8c-2d 1