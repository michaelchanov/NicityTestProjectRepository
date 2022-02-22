package com.nicity.domain.use_cases.get_news_by_key

import com.nicity.domain.common.ResultState
import com.nicity.domain.models.News
import com.nicity.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsByKeyUseCase @Inject constructor(val newsRepository: NewsRepository) {

    suspend fun execute(key: String): Flow<ResultState<News?>> {
        return newsRepository.getNewsByKey(key = key)
    }
}