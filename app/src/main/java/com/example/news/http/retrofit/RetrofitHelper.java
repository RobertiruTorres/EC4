package com.example.news.http.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    public static Retrofit instance;

    public RetrofitHelper() {
    }
    public static Retrofit getInstance(){
        if (instance == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://newsapi.org/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getLogginBuilder().build())
                    .build();
            instance = retrofit;
        }
        return instance;
    }


    public static OkHttpClient.Builder getLogginBuilder(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor((interceptor));
        return builder;

    }
}
