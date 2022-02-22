package com.nicity.nicitytestproject.presentation.detail.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.nicity.nicitytestproject.R
import com.nicity.nicitytestproject.databinding.FragmentDetailBinding
import com.nicity.nicitytestproject.presentation.common.DataState
import com.nicity.nicitytestproject.presentation.detail.viewmodel.DetailViewModel
import com.nicity.nicitytestproject.presentation.detail.viewmodel.DetailViewModel.Companion.passedNews
import com.nicity.nicitytestproject.presentation.detail.viewmodel.DetailViewModelFactory
import com.nicity.nicitytestproject.presentation.feed.view.FeedFragment.Companion.NEWS_VO_EXTRA_KEY
import com.nicity.nicitytestproject.presentation.liked.view.LikedNewsFragment.Companion.LIKED_NEWS_EXTRA_KEY
import com.nicity.nicitytestproject.presentation.models.NewsVO
import com.nicity.nicitytestproject.utils.delegates.viewBinding
import com.nicity.nicitytestproject.utils.extensions.setImage
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class DetailFragment : Fragment(R.layout.fragment_detail) {

    companion object {
        private const val UNKNOWN_ERROR_TEXT = "Неизвестная ошибка"
    }

    private val binding by viewBinding(FragmentDetailBinding::bind)
    private var viewModel: DetailViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        handleGetNewsByKey()
        passedNews = arguments?.getParcelable(NEWS_VO_EXTRA_KEY)

        if (passedNews == null) {
            passedNews = arguments?.getParcelable(LIKED_NEWS_EXTRA_KEY)
        }

        passedNews?.let { passedNews ->
            getNewsByKey(key = passedNews.title + passedNews.image +
                    passedNews.source + passedNews.description)
        }

        setContent(news = passedNews)
        setClickListeners()
    }

    private fun getNewsByKey(key: String) {
        viewModel?.getNewsByKey(key = key)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            factory = DetailViewModelFactory(context = requireActivity().applicationContext)
        )[DetailViewModel::class.java]
    }

    private fun setClickListeners() {
        binding.ivLike.setOnClickListener {
            animateLike()
        }
    }

    private fun animateLike() {
        binding.ivLike.animate().apply {
            if (viewModel?.newsLikeIsClicked == true) {
                deleteLikedNews()
            } else {
                saveLikedNews()
            }
            duration = 1000
            rotationYBy(360F)
        }
    }

    private fun deleteLikedNews() {
        binding.ivLike.setImageResource(R.drawable.ic_like_disabled)
        passedNews?.let { passedNews -> viewModel?.deleteLikedNews(news = passedNews) }
        viewModel?.newsLikeIsClicked = false
    }

    private fun saveLikedNews() {
        binding.ivLike.setImageResource(R.drawable.ic_like_enabled)
        passedNews?.let { passedNews -> viewModel?.saveLikedNews(likedNews = passedNews) }
        viewModel?.newsLikeIsClicked = true
    }

    private fun setContent(news: NewsVO?) {
        lifecycleScope.launch(Main) {
            with(binding) {
                tvNewsTitle.text = news?.title
                tvNewsDescription.text = news?.description
                tvNewsSource.text = getString(R.string.fragment_detail_news_source_text)
                    .format(news?.source)
                setImage(news)
            }
        }
    }

    private fun setImage(news: NewsVO?) {
        if (news?.image == null) {
            binding.ivNewsPicture.setImageResource(R.drawable.no_picture_placeholder)
        } else {
            binding.ivNewsPicture.setImage(news.image, requireActivity().applicationContext)
        }
    }

    private fun handleGetNewsByKey() {
        lifecycleScope.launchWhenStarted {
            viewModel?.news?.collect { news ->
                when (news) {
                    is DataState.Loading -> {}

                    is DataState.Success -> {
                        handleNewsSuccess()
                    }

                    is DataState.Error -> {
                        handleNewsError(errorMessage = news.message)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun handleNewsError(errorMessage: String?) {
        Snackbar.make(
            requireView(), errorMessage ?: UNKNOWN_ERROR_TEXT,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun handleNewsSuccess() {
        binding.ivLike.setImageResource(R.drawable.ic_like_enabled)
        viewModel?.newsLikeIsClicked = true
    }
}