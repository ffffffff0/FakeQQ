package net;

import Msg.Msg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientFrame extends JFrame implements Runnable {

    private JComboBox<String> JFriends;
    private Socket socket;
    private JTextArea jta;
    private JTextField jtf;

    // 客户端
    ClientFrame(String name, Socket socket, ObjectOutputStream write) {
        this.socket = socket;
        setTitle(name + "的聊天窗口");
        // 界面大小
        setBounds(490, 200, 400, 450);

        // 退出
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 文本区域
        JPanel textPane = new JPanel();
        textPane.setLayout(null);

        // 底部按钮
        JPanel footButton = new JPanel();

        // 用户列表
        JFriends = new JComboBox<>();
        footButton.add(JFriends);

        // 文本框
        jtf = new JTextField();
        jtf.setBounds(0, 260, 400, 100);
        textPane.add(jtf);

        // 文本域
        jta = new JTextArea(8, 12);

        // 自动换行
        jta.setLineWrap(true);

        // 加入滚动条
        JScrollPane sp = new JScrollPane(jta);
        sp.setBounds(0, 0, 400, 250);

        // 超过文本域才会显示
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // 加入面板
        textPane.add(sp);

        // 日期格式
        SimpleDateFormat dataForm = new SimpleDateFormat("YYYY/MM/dd/ hh:mm:ss");

        // 发送按钮
        JButton jbn = new JButton("发送");
        jbn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 定义消息
                Msg msg = new Msg();
                msg.setMessageType("chat");
                String idFriend = JFriends.getSelectedItem().toString();
                msg.setGetter(idFriend);
                msg.setSender(name);
                msg.setContent(jtf.getText());
                msg.setDate(dataForm.format(new Date()));

                // 打印
                String msgStr = msg.getSender() + "给" + msg.getGetter() + "发消息了";
                System.out.println(msgStr);
                jtf.setText("");
                // 发消息
                sendMsg(msg);
                try {
                    write.writeObject(msg);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        footButton.add(jbn);

        // 退出按钮
        JButton exitButton = new JButton("退出");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 退出
                Msg msg = new Msg();
                msg.setMessageType("exit");
                msg.setSender(name);
                try {
                    write.writeObject(msg);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    write.writeObject(msg);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                ClientFrame.this.dispose();
            }
        });
        footButton.add(exitButton);

        // 加入
        this.add(footButton, BorderLayout.SOUTH);
        this.add(textPane);
        this.setVisible(true);
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                // 重写run方法
                ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
                Msg msg = (Msg) read.readObject();
                switch (msg.getMessageType()) {
                    case "chat":
                        // 发送消息
                        sendMsg(msg);
                        break;
                    case "returnFriend":
                        // 更新在线列表
                        String[] friends = msg.getContent().split(" ");
                        updateFriend(friends);
                        break;
                    case "delFriend":
                        // 删除离线好友
                        JFriends.removeItem(msg.getContent());
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 更新在线列表
    private void updateFriend(String[] Friends) {
        for (String friend : Friends) {
            JFriends.addItem(friend);
        }
    }

    // 发送消息
    private void sendMsg(Msg msg) {
        jta.append(msg.getSender() + "---->" + msg.getGetter() + msg.getDate() + "\r\n");
        jta.append("   " + msg.getContent() + "\r\n");
    }
}
