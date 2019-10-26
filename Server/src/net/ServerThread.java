package net;

import Msg.Msg;
import Database.Operator;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket connection;

    ServerThread(Socket connection) {
        this.connection = connection;
    }

    // 返回列表
    private void returnFriend(String[] keys, Msg msg, StringBuilder result, String flag) throws IOException {
        for (String key : keys) {
            if (!key.equals(msg.getSender())) {
                result.append(key).append("-");
                // 更新在有新的登录时，将新登录的用户返回给其他在线用户
                Socket socket = ServerManger.getSocket(key);
                ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
                Msg m = new Msg();
                m.setMessageType(flag);
                m.setContent(msg.getSender());
                write.writeObject(m);
            }
        }
        // 将在线用户返回给新登录的用户
        ObjectOutputStream write = new ObjectOutputStream(connection.getOutputStream());
        msg.setContent(result.toString());
        msg.setGetter(msg.getSender());
        System.out.println(msg.getMessageType() + msg.getContent());
        write.writeObject(msg);
    }

    // run
    @Override
    public void run() {
        try {
            Operator op = new Operator();
            ObjectInputStream read = new ObjectInputStream(connection.getInputStream());
            while (true) {
                Msg msg = (Msg) read.readObject();
                System.out.println(msg.getMessageType());
                switch (msg.getMessageType()) {
                    case "chat":
                        // 聊天
                        Socket getter = ServerManger.getSocket(msg.getGetter());
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
                        returnFriend(keys, msg, result, "returnFriend");
                        break;
                    case "exit":
                        // 退出刷新界面在线列表
                        String delName = msg.getSender();
                        // 从map中删除
                        ServerManger.removeSocket(delName);
                        // 发送消息
                        msg.setMessageType("delFriend");
                        String[] list = ServerManger.getAllSocket();
                        StringBuilder re = new StringBuilder("");
                        // 返回列表
                        returnFriend(list, msg, re, "delFriend");
//                        this.interrupt();
                        return;
                    case "signUp":
                        // 注册
                        String signName = msg.getContent().split(",")[0];
                        String signPassword = msg.getContent().split(",")[1];
                        boolean flag = op.signUp(signName, signPassword);
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
