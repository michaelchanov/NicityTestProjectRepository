package com.nicity.nicitytestproject.presentation.feed.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nicity.nicitytestproject.BuildConfig
import com.nicity.nicitytestproject.R
import com.nicity.nicitytestproject.databinding.FragmentFeedBinding
import com.nicity.nicitytestproject.presentation.common.DataState
import com.nicity.nicitytestproject.presentation.common.NavigationFragment.Companion.NAVIGATION_FROM_MENU_EXTRA_KEY
import com.nicity.nicitytestproject.presentation.detail.view.DetailFragment
import com.nicity.nicitytestproject.presentation.feed.adapters.FeedAdapter
import com.nicity.nicitytestproject.presentation.feed.adapters.NewsClick
import com.nicity.nicitytestproject.presentation.feed.viewmodel.FeedViewModel
import com.nicity.nicitytestproject.presentation.feed.viewmodel.FeedViewModelFactory
import com.nicity.nicitytestproject.presentation.models.NewsVO
import com.nicity.nicitytestproject.utils.delegates.viewBinding
import com.nicity.nicitytestproject.utils.extensions.navigateToFragment

class FeedFragment : Fragment(R.layout.fragment_feed), NewsClick {

    companion object {
        const val NEWS_API_KEY = BuildConfig.NEWS_API_KEY
        const val GET_NEWS_LANGUAGES_QUERY = "en"
        const val NEWS_VO_EXTRA_KEY = "NEWS_VO_EXTRA_KEY"
        private var isFirstLaunch = true
        private var cachedNews = listOf<NewsVO>()
    }

    private val binding by viewBinding(FragmentFeedBinding::bind)
    private var viewModel: FeedViewModel? = null
    private val feedAdapter = FeedAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        collectNewsStateFlow()
        initViewModel()

        if (isFirstLaunch) {
            viewModel?.getNews(accessKey = NEWS_API_KEY, languages = GET_NEWS_LANGUAGES_QUERY)
            isFirstLaunch = false
        }

        if (arguments?.getBoolean(NAVIGATION_FROM_MENU_EXTRA_KEY) == true && !isFirstLaunch) {
            setCachedNewsToFeedAdapter(cachedNews = cachedNews)
        }

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setNewsRecyclerView()
        refreshContent()
    }

    private fun refreshContent() {
        with(binding.swipeRefreshLayout) {
            setOnRefreshListener {
                stopShimmerAnimation()
                viewModel?.getNews(accessKey = NEWS_API_KEY, languages = GET_NEWS_LANGUAGES_QUERY)
                isRefreshing = false
            }
        }
    }

    private fun setCachedNewsToFeedAdapter(cachedNews: List<NewsVO>) {
        feedAdapter.submitList(cachedNews)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this, FeedViewModelFactory(
                context = requireActivity().applicationContext
            )
        )[FeedViewModel::class.java]
    }

    private fun setNewsRecyclerView() {
        with(binding.feedNewsRV) {
            layoutManager = LinearLayoutManager(requireContext())
            feedAdapter.newsClickImpl = this@FeedFragment
            adapter = feedAdapter
        }
    }

    private fun startShimmerAnimation() {
        binding.feedNewsRV.isVisible = false
        with(binding.shimmerFrameLayout) {
            startShimmer()
            isVisible = true
        }
    }

    private fun stopShimmerAnimation() {
        with(binding.shimmerFrameLayout) {
            stopShimmer()
            isVisible = false
        }
        binding.feedNewsRV.isVisible = true
    }

    private fun collectNewsStateFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel?.listOfNewsState?.collect { listOfNewsState ->

                when (listOfNewsState) {
                    is DataState.Loading -> startShimmerAnimation()

                    is DataState.Success -> {
                            handleNewsSuccess(listOfNews = listOfNewsState.data)
                    }

                    is DataState.Error -> {
                        handleNewsError(errorMessage = listOfNewsState.message)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun handleNewsSuccess(listOfNews: List<NewsVO>?) {
        stopShimmerAnimation()
        listOfNews?.let {
            feedAdapter.submitList(listOfNews)
            cachedNews = listOfNews
        }
    }

    private fun handleNewsError(errorMessage: String?) {
        stopShimmerAnimation()
        if (errorMessage != null) {
            Snackbar.make(
                binding.root as View, errorMessage,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun handleNewsClick(news: NewsVO) {
        val bundle = Bundle()
        bundle.putParcelable(NEWS_VO_EXTRA_KEY, news)

        val fragment = DetailFragment()
        fragment.arguments = bundle

        requireActivity().supportFragmentManager.navigateToFragment(
            fragment = fragment,
            addToBackStack = true,
            primaryContainer = false
        )
    }
}