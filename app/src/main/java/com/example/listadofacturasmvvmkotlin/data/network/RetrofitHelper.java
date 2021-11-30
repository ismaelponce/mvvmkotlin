package com.example.listadofacturasmvvmkotlin.data.network;


import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static final String BASE_URL = "https://viewnextandroid.mocklab.io/";
    
    public retrofit2.Retrofit getRetrofit() {
        return new retrofit2.Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
