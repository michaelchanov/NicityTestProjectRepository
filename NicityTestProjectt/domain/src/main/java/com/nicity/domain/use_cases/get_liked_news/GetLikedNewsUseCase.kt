package com.nicity.domain.use_cases.get_liked_news

import com.nicity.domain.common.ResultState
import com.nicity.domain.models.News
import com.nicity.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLikedNewsUseCase @Inject constructor(val newsRepository: NewsRepository) {

    suspend fun execute(): Flow<ResultState<List<News>>> {
        return newsRepository.getLikedNews()
    }
}