package com.nicity.nicitytestproject.presentation.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicity.domain.common.ResultState
import com.nicity.domain.use_cases.delete_liked_news.DeleteLikedNewsUseCase
import com.nicity.domain.use_cases.get_news_by_key.GetNewsByKeyUseCase
import com.nicity.domain.use_cases.save_liked_news.SaveLikedNewsUseCase
import com.nicity.nicitytestproject.presentation.common.DataState
import com.nicity.nicitytestproject.presentation.models.NewsVO
import com.nicity.nicitytestproject.presentation.models.toNews
import com.nicity.nicitytestproject.utils.extensions.toNewsVO
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DetailViewModel(
    private val saveLikedNewsUseCase: SaveLikedNewsUseCase,
    private val deleteLikedNewsUseCase: DeleteLikedNewsUseCase,
    private val getNewsByKeyUseCase: GetNewsByKeyUseCase
) : ViewModel() {

    companion object {
        var passedNews: NewsVO? = null
    }

    private val _news = MutableStateFlow<DataState<NewsVO>>(DataState.Empty())
    val news: StateFlow<DataState<NewsVO>> = _news
    var newsLikeIsClicked: Boolean = false

    fun saveLikedNews(likedNews: NewsVO) {
        viewModelScope.launch(IO) {
            saveLikedNewsUseCase.execute(likedNews = likedNews.toNews())
        }
    }

    fun deleteLikedNews(news: NewsVO) {
        viewModelScope.launch(IO) {
            deleteLikedNewsUseCase.execute(news = news.toNews())
        }
    }

    fun getNewsByKey(key: String) {
        viewModelScope.launch(IO) {
            getNewsByKeyUseCase.execute(key = key).onEach { resultState ->
                when (resultState) {
                    is ResultState.Success ->
                        resultState.data?.let { news ->
                            _news.value = DataState.Success(data = news.toNewsVO()) }

                    is ResultState.Error ->
                        _news.value = DataState.Error(message = resultState.message)

                    is ResultState.Loading ->
                        _news.value = DataState.Loading()
                }
            }.collect()
        }
    }
}