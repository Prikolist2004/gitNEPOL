package com.example.git1;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

interface FlowersAPI {
    @GET("feeds/flowers.json")
    Call<List<com.example.flow.Flower>> getData();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://services.hanselandpetal.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}