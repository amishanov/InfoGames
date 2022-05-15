package com.example.infogames.retrofit;



import com.example.infogames.model.Review;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReviewService {
    @POST("v1/reviews")
    Call<Void> createReview(@Body Review review);
}
