package com.example.infogames;

import com.example.infogames.model.User;

public class Data {
    private static Data instance;
    private User user;

    private Data() {
        user = new User();
        System.out.println("DATA CONSTRUCTOR");
    }

    public static void initInstance() {
        if (instance == null)
            instance = new Data();
    }

    public static Data getInstance() {
        if (instance == null)
            instance = new Data();
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setScore(int score) {
        user.setScore(score);
    }



    public int getScore() {
        return user.getScore();
    }

}
