package com.nicity.nicitytestproject.presentation.liked.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nicity.nicitytestproject.R
import com.nicity.nicitytestproject.databinding.FragmentLikedNewsBinding
import com.nicity.nicitytestproject.presentation.common.DataState
import com.nicity.nicitytestproject.presentation.detail.view.DetailFragment
import com.nicity.nicitytestproject.presentation.liked.adapters.LikedNewsAdapter
import com.nicity.nicitytestproject.presentation.liked.adapters.LikedNewsClick
import com.nicity.nicitytestproject.presentation.liked.viewmodel.LikedNewsViewModel
import com.nicity.nicitytestproject.presentation.liked.viewmodel.LikedNewsViewModelFactory
import com.nicity.nicitytestproject.presentation.models.NewsVO
import com.nicity.nicitytestproject.utils.delegates.viewBinding
import com.nicity.nicitytestproject.utils.extensions.navigateToFragment

class LikedNewsFragment : Fragment(R.layout.fragment_liked_news), LikedNewsClick {

    companion object {
        const val LIKED_NEWS_EXTRA_KEY = "LIKED_NEWS_EXTRA_KEY"
    }

    private val binding by viewBinding(FragmentLikedNewsBinding::bind)
    private val likedNewsAdapter = LikedNewsAdapter()
    private var viewModel: LikedNewsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initLikedNewsViewModel()
        collectNewsStateFlow()

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setLikedNewsRecyclerView()
        setPullToRefresh()
        viewModel?.getLikedNews()
    }

    private fun setPullToRefresh() {
        with(binding.swipeRefreshLayout) {
            setOnRefreshListener {
                stopShimmerAnimation()
                viewModel?.getLikedNews()
                isRefreshing = false
            }
        }
    }

    private fun initLikedNewsViewModel() {
        viewModel = ViewModelProvider(
            this, LikedNewsViewModelFactory(
                context = requireActivity().applicationContext)
        )[LikedNewsViewModel::class.java]
    }

    private fun setLikedNewsRecyclerView() {
        with(binding.likedNewsRV) {
            layoutManager = LinearLayoutManager(requireContext())
            likedNewsAdapter.likedNewsClickImpl = this@LikedNewsFragment
            adapter = likedNewsAdapter
        }
    }

    private fun collectNewsStateFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel?.likedNews?.collect { listOfNewsState ->

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
            likedNewsAdapter.submitList(listOfNews)
        }
    }

    private fun handleNewsError(errorMessage: String?) {
        stopShimmerAnimation()
        if (errorMessage != null) {
            Snackbar.make(binding.root as View,
                errorMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun startShimmerAnimation() {
        binding.likedNewsRV.isVisible = false
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
        binding.likedNewsRV.isVisible = true
    }

    override fun handleLikedNewsClick(news: NewsVO) {
        val bundle = Bundle()
        bundle.putParcelable(LIKED_NEWS_EXTRA_KEY, news)

        val fragment = DetailFragment()
        fragment.arguments = bundle

        requireActivity().supportFragmentManager.navigateToFragment(
            fragment = fragment,
            addToBackStack = true,
            primaryContainer = false
        )
    }
}