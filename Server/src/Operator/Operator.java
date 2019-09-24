package Operator;

import Database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Operator {
    // 登录验证
    public boolean logIn(String name, String password) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            String SQL = "select * from info where user=? and password=?";
            conn = Database.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, name);
            statement.setString(2, password);
            rs = statement.executeQuery();
            if (rs.next()) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.close(conn, statement, rs);
        }
        return result;
    }

    // 注册
    public boolean signUp(String name, String password) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            String SQL = "insert into info(user, password)values(?,?)";
            conn = Database.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, name);
            statement.setString(2, password);
            statement.execute();
            System.out.println("注册成功");
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.close(conn, statement);
        }
        return result;
    }
}
