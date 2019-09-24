package net;

import Msg.Msg;
import Operator.Operator;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket connection;
    private ObjectInputStream read;

    ServerThread(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            int count = 0;
            Operator op = new Operator();

            while (true) {
                if (count++ == 0) {
                    read = new ObjectInputStream(connection.getInputStream());
                }
                Msg msg = (Msg) read.readObject();
                Socket getter = ServerManger.getSocket(msg.getGetter());
                System.out.println(msg.getMessageType());
                switch (msg.getMessageType()) {
                    case "chat":
                        // 聊天
                        ObjectOutputStream write = new ObjectOutputStream(getter.getOutputStream());
                        write.writeObject(msg);
                        break;
                    case "logIn":
                        // 登录
                        String name = msg.getContent().split(",")[0];
                        String password = msg.getContent().split(",")[1];
                        boolean isSuccess = op.logIn(name, password);
                        write = new ObjectOutputStream(connection.getOutputStream());
                        if (isSuccess) {
                            System.out.println("登录成功");
                            msg.setContent("success");
                            msg.setGetter(msg.getSender());
                            System.out.println(msg.getContent());
                            write.writeObject(msg);
                            // 增加socket
                            ServerManger.addSocket(msg.getSender(), connection);
                        } else {
                            System.out.println("登录失败");
                            msg.setContent("fail");
                            System.out.println(msg.getContent());
                            write.writeObject(msg);
                        }
                        break;
                    case "getFriend":
                        // 刷新好友列表
                        // 获得好友在线列表
                        msg.setMessageType("returnFriend");
                        String[] keys = ServerManger.getAllSocket();
                        StringBuilder result = new StringBuilder("");
                        // 返回列表
                        for (String key : keys) {
                            if (!key.equals(msg.getSender())) {
                                result.append(key).append(" ");
                                // 更新在有新的登录时
                                Socket s = ServerManger.getSocket(key);
                                write = new ObjectOutputStream(s.getOutputStream());
                                Msg m = new Msg();
                                m.setMessageType("returnFriend");
                                m.setSender(msg.getSender());
                                m.setContent(msg.getSender());
                                m.setGetter(key);
                                write.writeObject(m);
                            }
                        }
                        write = new ObjectOutputStream(connection.getOutputStream());
                        msg.setContent(result.toString());
                        msg.setGetter(msg.getSender());
                        System.out.println(msg.getMessageType() + msg.getContent());
                        write.writeObject(msg);
                        break;
                    case "exit":
                        // 退出刷新界面在线列表
                        String delName = msg.getSender();
                        ServerManger.removeSocket(delName);
                        msg.setMessageType("delFriend");
                        String[] list = ServerManger.getAllSocket();
                        StringBuilder re = new StringBuilder("");
                        // 返回列表
                        for (String key : list) {
                            if (!key.equals(msg.getSender())) {
                                re.append(key).append(" ");
                                // 更新在有新的登录时
                                Socket s = ServerManger.getSocket(key);
                                write = new ObjectOutputStream(s.getOutputStream());
                                Msg m = new Msg();
                                m.setMessageType("delFriend");
                                m.setSender(msg.getSender());
                                m.setContent(msg.getSender());
                                m.setGetter(key);
                                write.writeObject(m);
                            }
                        }
                        write = new ObjectOutputStream(connection.getOutputStream());
                        msg.setContent(re.toString());
                        msg.setGetter(msg.getSender());
                        System.out.println(msg.getMessageType() + msg.getContent());
                        write.writeObject(msg);
                        break;
                    case "signUp":
                        // 注册
                        String sname = msg.getContent().split(",")[0];
                        String spassword = msg.getContent().split(",")[1];
                        boolean flag = op.signUp(sname, spassword);
                        write = new ObjectOutputStream(connection.getOutputStream());
                        if (flag) {
                            msg.setContent("success");
                            msg.setGetter(msg.getSender());
                            System.out.println(msg.getContent());
                            write.writeObject(msg);
                        } else {
                            msg.setContent("fail");
                            System.out.println(msg.getContent());
                            write.writeObject(msg);
                        }
                        break;
                    default:
                        System.out.println("消息错误！");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
