package com.example.news.http.retrofit;

import com.example.news.http.response.ArticlesResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ArticlesService {
    @GET("everything?q=tesla&language=es&apiKey=b15cbe35c30e44bab9ffbc7e9c25305c") // Reemplaza con la ruta del endpoint
    Call<ArticlesResponse> getNews(); // Define el tipo de respuesta esperada (NewsResponse en este caso)
}
