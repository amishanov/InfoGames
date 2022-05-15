package com.example.infogames;

import com.example.infogames.model.User;
import com.example.infogames.model.UserData;
import com.example.infogames.retrofit.RetrofitService;
import com.example.infogames.retrofit.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    public void sendUserData() {
        //TODO Отправка
        UserService userService = retrofitService.getRetrofit().create(UserService.class);
        UserData userData = new UserData(user.getToken(), user.getScore(), user.getAccess(),
                user.getTestsBests(), user.getGamesBests());
        userService.updateData(userData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    System.out.println("sendUserDa  ta: PASS");
                } else if (response.code() == 304) {
                    System.out.println("sendUserData: Failed 304");
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("sendUserData: Failed, " + t);
            }
        });
    }

}
