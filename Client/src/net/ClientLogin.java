package net;

import Msg.Msg;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientLogin extends JFrame {
    private JPanel cPanel;
    private JTextField nameField;
    private JPasswordField passField;

    public ClientLogin() {
        setTitle("登录界面");
        setBounds(490, 290, 300, 300);
        cPanel = new JPanel();
        cPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(cPanel);
        cPanel.setLayout(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 用户名
        JLabel namej = new JLabel("用户名");
        namej.setBounds(50, 50, 90, 30);
        cPanel.add(namej);

        nameField = new JTextField();
        nameField.setBounds(100, 50, 90, 30);
        cPanel.add(nameField);
        nameField.setColumns(10);

        // 密码
        JLabel passj = new JLabel("密码");
        passj.setBounds(50, 100, 90, 30);
        cPanel.add(passj);

        passField = new JPasswordField();
        passField.setBounds(100, 100, 90, 30);
        cPanel.add(passField);
        passField.setColumns(10);

        // 登录
        JButton logButton = new JButton("登录");
        logButton.setBounds(50, 150, 90, 30);
        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Socket socket = null;
                try {
                    String username = nameField.getText();
                    String password = new String(passField.getPassword());
                    // socket
                    socket = new Socket("127.0.0.1", 8888);
                    ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
                    Msg msg = new Msg();
                    // Message Type
                    // 登录 logIn
                    // 获得好友 getFriend
                    // 注册 signUp
                    // 返回好友列表 returnFriend
                    // 退出 exit
                    // 聊天 chat
                    //
                    msg.setMessageType("logIn");
                    msg.setSender(username);
                    msg.setContent(username + "," + password);
                    System.out.println(msg.getMessageType() + msg.getContent());
                    write.writeObject(msg);
                    // 登录验证
                    if (username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(cPanel, "请填写用户名或密码");
                    } else {
                        ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
                        msg = (Msg) read.readObject();
                        if (msg.getContent().equals("success")) {
                            // 登录成功
                            msg.setMessageType("getFriend");
                            msg.setSender(msg.getGetter());
                            System.out.println(msg.getMessageType() + msg.getContent());
                            write.writeObject(msg);
                            // 启动ClientFrame
                            new ClientFrame(username, socket, write);
                            ClientLogin.this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(cPanel, "登录失败");
                        }
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        cPanel.add(logButton);

        // 注册
        JButton signButton = new JButton("注册");
        signButton.setBounds(150, 150, 90, 30);
        signButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Socket socket = null;
                try {
                    String name = nameField.getText();
                    String password = new String(passField.getPassword());
                    // socket
                    socket = new Socket("127.0.0.1", 8888);
                    ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
                    Msg msg = new Msg();
                    msg.setMessageType("signUp");
                    msg.setSender(name);
                    msg.setContent(name + "," + password);
                    System.out.println(msg.getMessageType() + msg.getContent());
                    write.writeObject(msg);
                    ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
                    msg = (Msg) read.readObject();
                    if (name.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(cPanel, "用户名或密码不能为空");
                    } else {
                        if (msg.getContent().equals("success")) {
                            JOptionPane.showMessageDialog(cPanel, "注册成功");
                        } else {
                            JOptionPane.showMessageDialog(cPanel, "注册失败");
                        }
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        cPanel.add(signButton);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    ClientLogin li = new ClientLogin();
                    li.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

