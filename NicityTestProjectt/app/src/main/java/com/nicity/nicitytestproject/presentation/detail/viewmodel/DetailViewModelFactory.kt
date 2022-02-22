package com.nicity.nicitytestproject.presentation.detail.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nicity.data.datasource.database.room.database.AppDatabase
import com.nicity.data.datasource.database.room.storage_impl.RoomNewsStorageImpl
import com.nicity.data.datasource.network.retrofit.clients.RetrofitClient
import com.nicity.data.datasource.network.retrofit.network_impl.NewsNetworkImpl
import com.nicity.data.repository.NewsRepositoryImpl
import com.nicity.domain.repository.NewsRepository
import com.nicity.domain.use_cases.delete_liked_news.DeleteLikedNewsUseCase
import com.nicity.domain.use_cases.get_news_by_key.GetNewsByKeyUseCase
import com.nicity.domain.use_cases.save_liked_news.SaveLikedNewsUseCase
import com.nicity.nicitytestproject.R

class DetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val newsNetworkImpl by lazy(LazyThreadSafetyMode.NONE) {
        NewsNetworkImpl(newsService = RetrofitClient.retrofitService)
    }

    private val roomNewsStorageImpl by lazy(LazyThreadSafetyMode.NONE) {
        RoomNewsStorageImpl(newsDao = AppDatabase.getDatabase(context = context).getNewsDao())
    }

    private val newsRepositoryImpl: NewsRepository by lazy(LazyThreadSafetyMode.NONE) {
        NewsRepositoryImpl(
            context = context,
            newsStorage = roomNewsStorageImpl, newsNetwork = newsNetworkImpl
        )
    }

    private val saveLikedNewsUseCase = SaveLikedNewsUseCase(newsRepository = newsRepositoryImpl)
    private val deleteLikedNewsUseCase = DeleteLikedNewsUseCase(newsRepository = newsRepositoryImpl)
    private val getNewsByKeyUseCase = GetNewsByKeyUseCase(newsRepository = newsRepositoryImpl)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            DetailViewModel(
                saveLikedNewsUseCase = saveLikedNewsUseCase,
                deleteLikedNewsUseCase = deleteLikedNewsUseCase,
                getNewsByKeyUseCase = getNewsByKeyUseCase
            ) as T
        } else {
            throw IllegalStateException(
                context.getString(R.string.detail_view_model_factory_illegal_state_exception_text)
            )
        }
    }
}