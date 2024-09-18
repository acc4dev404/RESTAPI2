package com.example.RESTAPI.model;

import java.time.LocalDateTime;

public class User {

    private final String login;
    private final String password;
    private final LocalDateTime date;
    private final String email;

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.date = LocalDateTime.now();
    }

    public User(String login, String password, LocalDateTime date, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.date = date;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", date=" + date.toString() +
                ", email='" + email + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

}
