package com.example.infogames.retrofit;



import com.example.infogames.model.Review;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ReviewService {
    @POST("v1/reviews")
    Call<Boolean> createReview(@Header("Authorization") String token, @Body Review review);
}
