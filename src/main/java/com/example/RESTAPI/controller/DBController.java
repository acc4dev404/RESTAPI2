package com.example.RESTAPI.controller;

import com.example.RESTAPI.model.User;

import java.sql.*;

public class DBController {

    private final String url;
    private final String user;
    private final String password;

    public DBController(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public User getUser(String login) {
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            String sqlQuery = String.format("SELECT users.*, emails.email FROM users INNER JOIN emails ON users.login = emails.login WHERE users.login LIKE '%s';", login);
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                return new User(login, rs.getString("password"), rs.getTimestamp("date").toLocalDateTime(), rs.getString("email"));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.format("ERROR: %s\n", e.getMessage());
        }
        return null;
    }

    public int createUser(User user) {
        int rows = 0;
        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://192.168.1.45:5432/loadDB", "loaderDB", "ReSo999+")) {
            String sqlQuery = "INSERT INTO users (login, password, date) VALUES (?, ?, ?); \n INSERT INTO emails (login, email) VALUES (?, ?);";
            try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
                pstmt.setString(1, user.getLogin());
                pstmt.setString(2, user.getPassword());
                pstmt.setTimestamp(3, Timestamp.valueOf(user.getDate()));
                pstmt.setString(4, user.getLogin());
                pstmt.setString(5, user.getEmail());
                rows = pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.format("ERROR: %s\n", e.getMessage());
        }
        return rows;
    }

}
