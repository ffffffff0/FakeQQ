package test;

import net.ClientLogin;

import java.awt.*;

public class ClientTest {
    private static final int clientNumbers = 3;

    public static void main(String[] args) {

        for (int i = 0; i < clientNumbers; i++) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ClientLogin clog = new ClientLogin();
                    clog.setVisible(true);
                }
            });

        }
    }
}
