package com.example.infogames.retrofit;

import com.example.infogames.model.Theme;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ThemeService {

    @GET("v1/themes")
    Call<List<Theme>> getThemes();
}
