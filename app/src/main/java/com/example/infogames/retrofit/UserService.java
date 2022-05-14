package com.example.infogames.retrofit;

import com.example.infogames.model.AuthData;
import com.example.infogames.model.Token;
import com.example.infogames.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserService {

    @GET("v1/users/login")
    Call<User> getUserByLogin(@Body AuthData authData);

    @GET("v1/users")
    Call<User> getUserByToken(@Header("Authorization") String token);
}
