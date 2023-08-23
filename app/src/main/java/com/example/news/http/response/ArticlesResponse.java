package com.example.news.http.response;

import com.example.news.models.ArticleModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticlesResponse {
    @SerializedName("articles")
    public List<ArticleModel> articlesModelList;

    public List<ArticleModel> getArticlesModelList() {
        return articlesModelList;
    }

    public void setArticlesModelList(List<ArticleModel> articlesModelList) {
        this.articlesModelList = articlesModelList;
    }
}
