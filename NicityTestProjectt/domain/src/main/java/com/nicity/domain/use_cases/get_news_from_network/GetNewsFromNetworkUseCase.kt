package com.nicity.domain.use_cases.get_news_from_network

import com.nicity.domain.common.ResultState
import com.nicity.domain.models.News
import com.nicity.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsFromNetworkUseCase @Inject constructor(val newsRepository: NewsRepository) {

    suspend fun execute(accessKey: String, languages: String): Flow<ResultState<List<News>>> {
        return newsRepository.getNewsFromNetwork(accessKey = accessKey, languages = languages)
    }
}