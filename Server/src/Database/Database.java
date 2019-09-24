package Database;

import java.sql.*;

public class Database {
    private static final String DBURL = "com.mysql.cj.jdbc.Driver";
    private static final String JDBCDRIVER = "jdbc:mysql://localhost:3306/fakeqq?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "open";
    private static Connection conn = null;

    // 连接数据库
    public static Connection getConnection() {
        try {
            Class.forName(DBURL);
            conn = DriverManager.getConnection(JDBCDRIVER, USER, PASS);
//            System.out.println("数据库连接成功");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    //断开数据库连接
    public static void close(Connection con, Statement stat, ResultSet rs) {
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
    public static void close(Connection con, Statement stat) {
        close(con, stat, null);
    }
}
