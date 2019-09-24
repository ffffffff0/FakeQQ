package net;

import java.net.Socket;
import java.util.HashMap;

class ServerManger {
    private static HashMap<String, Socket> hm = new HashMap<String, Socket>();

    // 增加socket
    static void addSocket(String key, Socket soc) {
        hm.put(key, soc);
    }

    // 获得socket
    static Socket getSocket(String key) {
        return hm.get(key);
    }

    // 获得全部的socket
    static String[] getAllSocket() {
        String[] result = new String[hm.size()];
        int i = 0;
        for (String soc : hm.keySet()) {
            result[i++] = soc;
        }
        return result;
    }

    // 删除socket
    static void removeSocket(String key) {
        hm.remove(key);
    }

}
