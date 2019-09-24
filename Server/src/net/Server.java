package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static ServerSocket ss;

    // 构造方法
    private Server() {
        try {
            int port = 8888;
            ss = new ServerSocket(port);
            System.out.println("服务器启动了,等待客户端连接...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Socket tcpConn = ss.accept();
                System.out.println("有客户端连接了");
                ServerThread r = new ServerThread(tcpConn);
                r.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
