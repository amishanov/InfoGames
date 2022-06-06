package com.example.infogames.retrofit;

import com.example.infogames.model.User;
import com.example.infogames.model.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserService {

    @GET("v1/users/login")
    Call<User> getUserByLogin(@Header("Authorization") String logPass);

    @GET("v1/users")
    Call<User> getUserByToken(@Header("Authorization") String token);

    @POST("v1/users")
    Call<Void> createUser(@Body User user);

    @PUT("v1/users")
    Call<Void> updateData(@Body UserData userData);
}
