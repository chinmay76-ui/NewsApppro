package com.example.newsapppro.api

import com.example.newsapppro.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country") countryCode: String,
        @Query("page") pageNumber: Int
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int,
        @Query("language") language: String? = null,
        @Query("sortBy") sortBy: String? = null
    ): Response<NewsResponse>
}
