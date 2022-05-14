package com.example.infogames.model;

import java.util.Arrays;

public class User {
    private  int _id;
    private String email;
    private String login;
    private String password;
    private String token;
    private int score;
    private Boolean[] access;
    private Integer[] testsBests;
    private Integer[] gamesBests;

    public User() {}

    public User(int _id, String email, String login, String password,
                String token, int score, Boolean[] access,
                Integer[] testsBests, Integer[] gamesBests) {
        this._id = _id;
        this.email = email;
        this.login = login;
        this.password = password;
        this.token = token;
        this.score = score;
        this.access = access;
        this.testsBests = testsBests;
        this.gamesBests = gamesBests;
    }

    public void clone(User user) {
        // Возможны проблемы из-за ссылок на String
        this._id = user._id;
        this.email = user.email;
        this.login = user.login;
        this.password = user.password;
        this.token = user.token;
        this.score = user.score;
        this.access = Arrays.copyOf(user.access, user.access.length);
        this.testsBests = Arrays.copyOf(user.testsBests, user.testsBests.length);
        this.gamesBests = Arrays.copyOf(user.gamesBests, user.gamesBests.length);
    }

    @Override
    public String toString() {
        return "{" + "\"_id\":" +  _id +
                ", \"email\":\"" + email + '\"' +
                ", \"login\":\"" + login + '\"' +
                ", \"password\":\"" + password + '\"' +
                ", \"token\":\"" + token + '\"' +
                ", \"score\":" + score +
                ", \"access\":" + Arrays.toString(access) +
                ", \"testsBests\":" + Arrays.toString(testsBests) +
                ", \"gamesBests\":" + Arrays.toString(gamesBests) +
                '}';
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setAccess(Boolean[] access) {
        this.access = access;
    }

    public void setTestsBests(Integer[] testsBests) {
        this.testsBests = testsBests;
    }

    public void setGamesBests(Integer[] gamesBests) {
        this.gamesBests = gamesBests;
    }

    public int get_id() {
        return _id;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public int getScore() {
        return score;
    }

    public Boolean[] getAccess() {
        return access;
    }

    public Integer[] getTestsBests() {
        return testsBests;
    }

    public Integer[] getGamesBests() {
        return gamesBests;
    }
}