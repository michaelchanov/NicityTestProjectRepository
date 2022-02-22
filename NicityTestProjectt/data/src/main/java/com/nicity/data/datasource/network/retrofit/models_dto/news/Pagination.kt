package com.nicity.data.datasource.network.retrofit.models_dto.news

data class Pagination(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val total: Int
)