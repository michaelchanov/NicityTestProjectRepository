package com.nicity.data.repository

import android.content.Context
import com.nicity.data.R
import com.nicity.data.datasource.database.room.entities.NewsDB
import com.nicity.data.datasource.database.room.entities.toNews
import com.nicity.data.datasource.database.storage.NewsStorage
import com.nicity.data.datasource.database.utils.extensions.toNewsDB
import com.nicity.data.datasource.network.common.NewsNetwork
import com.nicity.data.datasource.network.retrofit.models_dto.news.toNews
import com.nicity.domain.common.ResultState
import com.nicity.domain.models.News
import com.nicity.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    val context: Context,
    val newsStorage: NewsStorage,
    val newsNetwork: NewsNetwork
) : NewsRepository {

    override suspend fun getNewsFromNetwork(accessKey: String, languages: String)
            : Flow<ResultState<List<News>>> = flow {
        try {
            emit(ResultState.Loading())
            emit(
                ResultState.Success(data = newsNetwork.getNews(
                    accessKey = accessKey,
                    languages = languages
                ).newsData.map { newsDtoItem ->
                    newsDtoItem.toNews()
                })
            )
        } catch (ex: HttpException) {
            emit(
                ResultState.Error(
                    message = ex.localizedMessage ?: context
                        .getString(R.string.news_repository_impl_http_exception_text)
                )
            )
        } catch (ex: IOException) {
            emit(
                ResultState.Error(
                    message = context
                        .getString(R.string.news_repository_impl_io_exception_text)
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun saveLikedNews(likedNews: News) {
        val newsDB = NewsDB(
            key = likedNews.title + likedNews.image + likedNews.source + likedNews.description,
            title = likedNews.title, image = likedNews.image, description = likedNews.description,
            source = likedNews.source
        )

        newsStorage.saveLikedNews(newsDB = newsDB)
    }

    override suspend fun getLikedNews(): Flow<ResultState<List<News>>> = flow {
        try {
            emit(ResultState.Loading())
            emit(ResultState.Success(data = newsStorage.getLikedNews().map { newsDB ->
                newsDB.toNews()
            }))
        } catch (ex: Exception) {
            emit(
                ResultState.Error(
                    message = ex.localizedMessage ?: context
                        .getString(R.string.news_repository_impl_database_exception_text)
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteLikedNews(news: News) {
        newsStorage.deleteLikedNews(newsDB = news.toNewsDB())
    }

    override suspend fun getNewsByKey(key: String): Flow<ResultState<News?>> = flow {
        try {
            emit(ResultState.Loading())
            emit(ResultState.Success(data = newsStorage.getNewsByKey(key = key)?.toNews()))
        } catch (ex: Exception) {
            emit(
                ResultState.Error(
                    message = ex.localizedMessage ?: context
                        .getString(R.string.news_repository_impl_database_exception_text)
                )
            )
        }
    }.flowOn(Dispatchers.IO)
}