package com.example.infogames.retrofit;

import com.example.infogames.model.Test;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TestService {
    @GET("v1/tests")
    Call<List<Test>> getTests();
}
