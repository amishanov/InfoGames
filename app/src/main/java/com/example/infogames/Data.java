package com.example.infogames;

import android.content.Context;

import com.example.infogames.model.Review;
import com.example.infogames.model.User;
import com.example.infogames.model.UserData;
import com.example.infogames.retrofit.RetrofitService;
import com.example.infogames.retrofit.ReviewService;
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
        UserData userData = new UserData(user.getToken(), user.getScore(), user.getProgress(), user.getAccess(),
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

    public String sendReview(Review review) {
        // TODO переделать под синхронный поток (Пользователь должен знать, что подключение не работает)
        ReviewService reviewService = retrofitService.getRetrofit().create(ReviewService.class);
        user.setToken("login60.2659194162054871");
        reviewService.createReview(user.getToken(), review).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 201)
                    System.out.println("SendReview: PASS");
                else
                    System.out.println("SendReview: FAILED 409");
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println("sendUserData: Failed, " + t);
            }

        });

        return "";
    }


}
