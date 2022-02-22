package com.nicity.domain.use_cases.delete_liked_news

import com.nicity.domain.models.News
import com.nicity.domain.repository.NewsRepository
import javax.inject.Inject

class DeleteLikedNewsUseCase @Inject constructor(val newsRepository: NewsRepository) {

    suspend fun execute(news: News) {
        newsRepository.deleteLikedNews(news = news)
    }
}