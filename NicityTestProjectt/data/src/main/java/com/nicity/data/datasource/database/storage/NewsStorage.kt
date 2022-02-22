package com.nicity.data.datasource.database.storage

import com.nicity.data.datasource.database.room.entities.NewsDB

interface NewsStorage {

    suspend fun saveLikedNews(newsDB: NewsDB)

    suspend fun getLikedNews(): List<NewsDB>

    suspend fun deleteLikedNews(newsDB: NewsDB)

    suspend fun getNewsByKey(key: String): NewsDB?
}