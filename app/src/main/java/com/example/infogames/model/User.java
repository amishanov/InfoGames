package com.example.infogames.model;

import java.util.Arrays;

public class User {
    private String id;
    private String email;
    private String login;
    private String password;
    private String token;
    private int score;
    private Boolean[] progress;
    private Boolean[] access;
    private Integer[] testsBests;
    private Integer[] gamesBests;

    public User() {
        //TODO Убрать, так как должно происходить чтение из файла
        progress = new Boolean[]{false, false, false, false, false, false};
        access = new Boolean[]{true, true, false, false, false, false};
        testsBests = new Integer[]{0, 0, 0, 0, 0, 0};
        gamesBests = new Integer[]{0, 0};
    }

    public User(String id, String email, String login, String password,
                String token, int score, Boolean[] progress, Boolean[] access,
                Integer[] testsBests, Integer[] gamesBests) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.password = password;
        this.token = token;
        this.progress = progress;
        this.score = score;
        this.access = access;
        this.testsBests = testsBests;
        this.gamesBests = gamesBests;
    }

    public User(String email, String login,
                String password, String token, int score, Boolean[] progress,
                Boolean[] access, Integer[] testsBests, Integer[] gamesBests) {
        this.email = email;
        this.login = login;
        this.password = password;
        this.token = token;
        this.score = score;
        this.progress = progress;
        this.access = access;
        this.testsBests = testsBests;
        this.gamesBests = gamesBests;
    }

    public void clone(User user) {
        // Возможны проблемы из-за ссылок на String
        this.id = user.id;
        this.email = user.email;
        this.login = user.login;
        this.password = user.password;
        this.token = user.token;
        this.score = user.score;
        this.progress = Arrays.copyOf(user.progress, user.progress.length);
        this.access = Arrays.copyOf(user.access, user.access.length);
        this.testsBests = Arrays.copyOf(user.testsBests, user.testsBests.length);
        this.gamesBests = Arrays.copyOf(user.gamesBests, user.gamesBests.length);
    }

    @Override
    public String toString() {
        return "{" + "\"_id\":" + id +
                ", \"email\":\"" + email + '\"' +
                ", \"login\":\"" + login + '\"' +
                ", \"password\":\"" + password + '\"' +
                ", \"token\":\"" + token + '\"' +
                ", \"score\":" + score +
                ", \"progress\":" + Arrays.toString(progress) +
                ", \"access\":" + Arrays.toString(access) +
                ", \"testsBests\":" + Arrays.toString(testsBests) +
                ", \"gamesBests\":" + Arrays.toString(gamesBests) +
                '}';
    }

    public void setId(String id) {
        this.id = id;
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

    public String getId() {
        return id;
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

    public Boolean[] getProgress() {
        return progress;
    }

    public void setProgress(Boolean[] progress) {
        this.progress = progress;
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
