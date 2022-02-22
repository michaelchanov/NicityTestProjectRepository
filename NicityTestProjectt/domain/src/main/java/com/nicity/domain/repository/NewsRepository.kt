package com.nicity.domain.repository

import com.nicity.domain.common.ResultState
import com.nicity.domain.models.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNewsFromNetwork(
        accessKey: String,
        languages: String
    ): Flow<ResultState<List<News>>>

    suspend fun saveLikedNews(likedNews: News)

    suspend fun getLikedNews(): Flow<ResultState<List<News>>>

    suspend fun deleteLikedNews(news: News)

    suspend fun getNewsByKey(key: String): Flow<ResultState<News?>>
}