package com.nicity.data.datasource.database.utils.extensions

import com.nicity.data.datasource.database.room.entities.NewsDB
import com.nicity.domain.models.News

fun News.toNewsDB(): NewsDB {
    return NewsDB(
        key = title + image + source + description,
        title = title,
        image = image,
        description = description,
        source = source
    )
}