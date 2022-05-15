package com.example.infogames.model;

public class UserData {
    private String token;
    private int score;
    private Boolean[] access;
    private Integer[] testsBests;
    private Integer[] gamesBests;

    public UserData() {}

    public UserData(String token, int score, Boolean[] access, Integer[] testsBests, Integer[] gamesBests) {
        this.token = token;
        this.score = score;
        this.access = access;
        this.testsBests = testsBests;
        this.gamesBests = gamesBests;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean[] getAccess() {
        return access;
    }

    public void setAccess(Boolean[] access) {
        this.access = access;
    }

    public Integer[] getTestsBests() {
        return testsBests;
    }

    public void setTestsBests(Integer[] testsBests) {
        this.testsBests = testsBests;
    }

    public Integer[] getGamesBests() {
        return gamesBests;
    }

    public void setGamesBests(Integer[] gamesBests) {
        this.gamesBests = gamesBests;
    }
}
