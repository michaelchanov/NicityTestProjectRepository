package com.nicity.data.datasource.network.retrofit.clients

import com.nicity.data.datasource.network.retrofit.common.Constants.BASE_URL
import com.nicity.data.datasource.network.retrofit.services.NewsService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    val retrofitService: NewsService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(NewsService::class.java)
}