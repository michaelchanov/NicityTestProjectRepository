package com.nicity.data.datasource.database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicity.domain.models.News

@Entity(tableName = "news_table")
data class NewsDB(
    @PrimaryKey(autoGenerate = false) val key: String, val title: String,
    val image: String?, val description: String, val source: String
)

fun NewsDB.toNews(): News {
    return News(
        title = title, image = image,
        description = description, source = source
    )
}