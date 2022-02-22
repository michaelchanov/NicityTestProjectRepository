package com.nicity.data.datasource.database.room.dao

import androidx.room.*
import com.nicity.data.datasource.database.room.entities.NewsDB

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveLikedNews(news: NewsDB)

    @Delete
    suspend fun deleteLikedNews(news: NewsDB)

    @Query("SELECT * FROM news_table")
    suspend fun getLikedNews(): List<NewsDB>

    @Query("SELECT * FROM news_table WHERE `key` = :key")
    suspend fun getNewsByKey(key: String): NewsDB?
}