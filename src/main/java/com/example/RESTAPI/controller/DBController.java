package com.example.RESTAPI.controller;

import com.example.RESTAPI.model.User;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class DBController {

    private final String url_conn = "jdbc:postgresql://192.168.1.45:5432/loadDB";
    private final String user_conn = "loaderDB";
    private final String password_conn = "ReSo999+";

    public DBController() {
    }

    public User getUser(String login) throws Exception {
        User user = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sqlQuery = String.format("SELECT users.*, emails.email FROM users INNER JOIN emails ON users.login = emails.login WHERE users.login LIKE '%s';", login);
        try {
            conn = DriverManager.getConnection(url_conn, user_conn, password_conn);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                user = new User(login, rs.getString("password"), rs.getTimestamp("date").toLocalDateTime(), rs.getString("email"));
            } else
                throw new Exception("User not found");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.format("ERROR: %s\n", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
        return user;
    }

    public int createUser(User user) {
        int rows = 0;
        String sqlQuery = "INSERT INTO users (login, password, date) VALUES (?, ?, ?); \n INSERT INTO emails (login, email) VALUES (?, ?);";
        try (Connection connection = DriverManager.getConnection(url_conn, user_conn, password_conn);
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getPassword());
            pstmt.setTimestamp(3, Timestamp.valueOf(user.getDate()));
            pstmt.setString(4, user.getLogin());
            pstmt.setString(5, user.getEmail());
            rows = pstmt.executeUpdate();
            if (rows == 0)
                throw new Exception("User has not been created");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.format("ERROR: %s\n", e.getMessage());
            throw new RuntimeException(e);
        }
        return rows;
    }

}
