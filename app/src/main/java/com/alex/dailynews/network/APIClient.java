package com.alex.dailynews.network;

import com.alex.dailynews.BuildConfig;
import com.alex.dailynews.model.NewsReply;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface APIClient {

//    String apiKey = BuildConfig.ApiKey;

    @GET("top-headlines?pageSize=100&apiKey=" + BuildConfig.NEWS_API_KEY)
    Call<NewsReply> getTopHeadlines(
            @Query("sources") String sources
    );

    @GET("everything?language=en&sortBy=popularity&pageSize=100&apiKey=" + BuildConfig.NEWS_API_KEY)
    Call<NewsReply> searchAnything(
            @Query("q") String q
    );

}
