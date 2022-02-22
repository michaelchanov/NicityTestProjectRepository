package com.nicity.nicitytestproject.presentation.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicity.domain.common.ResultState
import com.nicity.domain.use_cases.get_news_from_network.GetNewsFromNetworkUseCase
import com.nicity.nicitytestproject.presentation.common.DataState
import com.nicity.nicitytestproject.presentation.models.NewsVO
import com.nicity.nicitytestproject.utils.extensions.toNewsVO
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FeedViewModel(private val getNewsFromNetworkUseCase: GetNewsFromNetworkUseCase) :
    ViewModel() {

    private val _listOfNewsState = MutableStateFlow<DataState<List<NewsVO>>>(DataState.Empty())
    val listOfNewsState: StateFlow<DataState<List<NewsVO>>> = _listOfNewsState

    fun getNews(accessKey: String, languages: String) {
        viewModelScope.launch(IO) {
            getNewsFromNetworkUseCase.execute(accessKey = accessKey, languages = languages)
                .onEach { resultState ->
                    when (resultState) {
                        is ResultState.Success ->
                            _listOfNewsState.value =
                                DataState.Success(data = resultState.data?.map { news ->
                                    news.toNewsVO()
                                } ?: listOf())

                        is ResultState.Error ->
                            _listOfNewsState.value = DataState.Error(message = resultState.message)

                        is ResultState.Loading ->
                            _listOfNewsState.value = DataState.Loading()
                    }
                }.collect()
        }
    }
}