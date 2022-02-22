package com.nicity.data.datasource.network.retrofit.models_dto.news

import com.nicity.domain.models.News

data class NewsDataDto(
    val author: Any,
    val category: String,
    val country: String,
    val description: String,
    val image: String,
    val language: String,
    val published_at: String,
    val source: String,
    val title: String,
    val url: String
)

fun NewsDataDto.toNews(): News {
    return News(
        title = title, image = image, description = description, source = source
    )
}