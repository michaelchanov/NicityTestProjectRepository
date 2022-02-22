package com.nicity.nicitytestproject.presentation.liked.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicity.domain.common.ResultState
import com.nicity.domain.use_cases.get_liked_news.GetLikedNewsUseCase
import com.nicity.nicitytestproject.presentation.common.DataState
import com.nicity.nicitytestproject.presentation.models.NewsVO
import com.nicity.nicitytestproject.utils.extensions.toNewsVO
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LikedNewsViewModel(private val getLikedNewsUseCase: GetLikedNewsUseCase) : ViewModel() {

    private var _likedNews = MutableStateFlow<DataState<List<NewsVO>>>(DataState.Empty())
    val likedNews: StateFlow<DataState<List<NewsVO>>> = _likedNews

    fun getLikedNews() {
        viewModelScope.launch(IO) {
            getLikedNewsUseCase.execute().onEach { resultState ->
                when (resultState) {

                    is ResultState.Success ->
                        resultState.data?.let { listOfNews ->
                            _likedNews.value = DataState.Success(data = listOfNews.map { news ->
                                news.toNewsVO()
                            })
                        }

                    is ResultState.Error ->
                        _likedNews.value = DataState.Error(message = resultState.message)

                    is ResultState.Loading ->
                        _likedNews.value = DataState.Loading()
                }
            }.collect()
        }
    }
}