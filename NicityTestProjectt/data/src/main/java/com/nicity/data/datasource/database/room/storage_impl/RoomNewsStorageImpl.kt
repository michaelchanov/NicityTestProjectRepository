package com.nicity.data.datasource.database.room.storage_impl

import com.nicity.data.datasource.database.room.dao.NewsDao
import com.nicity.data.datasource.database.room.entities.NewsDB
import com.nicity.data.datasource.database.storage.NewsStorage
import javax.inject.Inject

class RoomNewsStorageImpl @Inject constructor(val newsDao: NewsDao) : NewsStorage {

    override suspend fun saveLikedNews(newsDB: NewsDB) {
        newsDao.saveLikedNews(news = newsDB)
    }

    override suspend fun getLikedNews(): List<NewsDB> {
        return newsDao.getLikedNews()
    }

    override suspend fun deleteLikedNews(newsDB: NewsDB) {
        newsDao.deleteLikedNews(news = newsDB)
    }

    override suspend fun getNewsByKey(key: String): NewsDB? {
        return newsDao.getNewsByKey(key = key)
    }
}