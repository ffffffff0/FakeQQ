package Database;

import java.sql.*;

class Database {
    private static final String DB_URL = "com.mysql.cj.jdbc.Driver";
    private static final String JDBC_DRIVER = "jdbc:mysql://localhost:3306/fakeqq?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "open";
    private static Connection conn = null;

    // 连接数据库
    static Connection getConnection() {
        try {
            Class.forName(DB_URL);
            conn = DriverManager.getConnection(JDBC_DRIVER, USER, PASS);
//            System.out.println("数据库连接成功");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    //断开数据库连接
    static void close(Connection con, Statement stat, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
            if (stat != null)
                stat.close();
            if (con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //重载close
    static void close(Connection con, Statement stat) {
        close(con, stat, null);
    }
}
