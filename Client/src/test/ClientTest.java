package test;

import net.ClientLogin;

public class ClientTest {
    private static final int clientNumbers = 3;

    public static void main(String[] args) {

        for (int i = 0; i < clientNumbers; i++) {
            ClientLogin clog = new ClientLogin();
            clog.setVisible(true);
        }
    }
}
