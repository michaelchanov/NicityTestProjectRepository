package com.nicity.data.datasource.network.common

import com.nicity.data.datasource.network.retrofit.models_dto.news.NewsDto

interface NewsNetwork {

    suspend fun getNews(accessKey: String, languages: String): NewsDto
}