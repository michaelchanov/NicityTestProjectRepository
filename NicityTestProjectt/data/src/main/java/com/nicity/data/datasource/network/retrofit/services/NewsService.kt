package com.nicity.data.datasource.network.retrofit.services

import com.nicity.data.datasource.network.retrofit.models_dto.news.NewsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("news")
    suspend fun getNews(
        @Query("access_key") accessKey: String,
        @Query("languages") languages: String
    ): NewsDto
}