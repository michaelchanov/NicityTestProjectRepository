package com.nicity.data.datasource.network.retrofit.models_dto.news

import com.google.gson.annotations.SerializedName

data class NewsDto(
    @SerializedName("data")
    val newsData: List<NewsDataDto>,
    val pagination: Pagination
)