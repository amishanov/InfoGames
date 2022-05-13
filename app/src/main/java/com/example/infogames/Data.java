package com.example.infogames;

public class Data {
    private static Data instance;
    private int score;

    private Data() {}

    public static void initInstance() {
        if (instance == null)
            instance = new Data();
    }

    public static Data getInstance() {
        if (null == instance)
            instance = new Data();
        return instance;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
