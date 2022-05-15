package com.example.infogames;

import com.example.infogames.model.User;
import com.example.infogames.retrofit.RetrofitService;


public class Data {
    private static Data instance;
    private User user;
    private RetrofitService  retrofitService;
    private boolean isLogin = false;

    private Data() {
        user = new User();
        retrofitService = new RetrofitService();
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

    public RetrofitService getRetrofitService() {
        return retrofitService;
    }

    public void setRetrofitService(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    public void setIsLogin(boolean login) {
        isLogin = login;
    }

    public boolean isLogin() {
        return isLogin;
    }

}
