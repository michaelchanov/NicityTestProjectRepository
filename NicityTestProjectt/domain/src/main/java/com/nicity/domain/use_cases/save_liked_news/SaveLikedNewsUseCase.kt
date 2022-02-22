package com.nicity.domain.use_cases.save_liked_news

import com.nicity.domain.models.News
import com.nicity.domain.repository.NewsRepository
import javax.inject.Inject

class SaveLikedNewsUseCase @Inject constructor(val newsRepository: NewsRepository) {

    suspend fun execute(likedNews: News) {
        newsRepository.saveLikedNews(likedNews = likedNews)
    }
}