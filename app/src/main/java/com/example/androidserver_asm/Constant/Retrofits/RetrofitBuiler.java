package com.example.androidserver_asm.Constant.Retrofits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuiler {
    private static final String url = "http://10.0.2.2:8580/";
    private static Retrofit build(){
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder().baseUrl(url).
                addConverterFactory(GsonConverterFactory.create(gson)).build();
    }
    public static <T> T createService (Class<T> service) {
        return build().create(service);
    }
}
