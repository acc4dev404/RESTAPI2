package com.example.RESTAPI.controller;

import com.example.RESTAPI.model.User;

import java.sql.*;
import java.time.LocalDateTime;

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

        Boolean isRecord = false;
        String password = null;
        LocalDateTime date = null;
        String email = null;

        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
            Statement stmt = conn.createStatement();
            String sqlQuery = String.format("SELECT users.*, emails.email FROM users INNER JOIN emails ON users.login = emails.login WHERE users.login LIKE '%s';", login);
            ResultSet rs = stmt.executeQuery(sqlQuery);
            isRecord = rs.next();
            if (isRecord) {
                password = rs.getString("password");
                date = rs.getTimestamp("date").toLocalDateTime();
                email = rs.getString("email");
                return new User(login, password, date, email);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isRecord)
            return new User(login, password, date, email);
        return null;
    }

    public int createUser(User user) {

        String login = user.getLogin();
        String password = user.getPassword();
        LocalDateTime date = user.getDate();
        String email = user.getEmail();
        int rows = 0;

        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://192.168.1.45:5432/loadDB", "loaderDB", "ReSo999+")) {

            String sqlQuery = "INSERT INTO users (login, password, date) VALUES (?, ?, ?); \n INSERT INTO emails (login, email) VALUES (?, ?);";

            try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
                pstmt.setString(1, login);
                pstmt.setString(2, password);
                pstmt.setTimestamp(3, Timestamp.valueOf(date));
                pstmt.setString(4, login);
                pstmt.setString(5, email);
                rows = pstmt.executeUpdate();
            } catch (SQLException e) {
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

}
