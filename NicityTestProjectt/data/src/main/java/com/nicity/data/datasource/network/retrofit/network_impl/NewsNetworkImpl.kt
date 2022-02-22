package com.nicity.data.datasource.network.retrofit.network_impl

import com.nicity.data.datasource.network.common.NewsNetwork
import com.nicity.data.datasource.network.retrofit.models_dto.news.NewsDto
import com.nicity.data.datasource.network.retrofit.services.NewsService
import javax.inject.Inject

class NewsNetworkImpl @Inject constructor(val newsService: NewsService): NewsNetwork {

    override suspend fun getNews(accessKey: String, languages: String): NewsDto {
        return newsService.getNews(accessKey = accessKey, languages = languages)
    }
}