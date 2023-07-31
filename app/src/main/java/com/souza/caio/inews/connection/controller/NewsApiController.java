package com.souza.caio.inews.connection.controller;

import com.souza.caio.inews.journal.JournalsListAPI;
import com.souza.caio.inews.news.RecentArcticlesListAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiController {
    @GET("top-headlines")
    Call<RecentArcticlesListAPI> getHeadlines(
            @Query("country") String country,
            @Query("apiKey") String apiKey,
            @Query("pageSize") int size,
            @Query("category") String category
    );

    @GET("top-headlines")
    Call<RecentArcticlesListAPI> getHeadlinesNoCategory(
            @Query("country") String country,
            @Query("apiKey") String apiKey,
            @Query("pageSize") int size
    );

    @GET("everything")
    Call<RecentArcticlesListAPI> getSearchResult(
            @Query("q") String query,
            @Query("apiKey") String apiKey,
            @Query("category") String category
    );

    @GET("everything")
    Call<RecentArcticlesListAPI> getSearchResultNoCategory(
            @Query("q") String query,
            @Query("apiKey") String apiKey
    );

    @GET("sources")
    Call<JournalsListAPI> getJornals(
            @Query("category") String category,
            @Query("apiKey") String apiKey

    );

    @GET("sources")
    Call<JournalsListAPI> getJornalsNoCategory(
            @Query("apiKey") String apiKey

    );

    @GET("everything")
    Call<RecentArcticlesListAPI> getArticlesJornalNoCategory(
            @Query("sources") String sources,
            @Query("apiKey") String apiKey,
            @Query("pageSize") int size
    );
}
