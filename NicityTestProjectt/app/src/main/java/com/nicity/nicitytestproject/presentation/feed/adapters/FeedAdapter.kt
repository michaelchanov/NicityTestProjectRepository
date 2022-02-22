package com.nicity.nicitytestproject.presentation.feed.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nicity.nicitytestproject.R
import com.nicity.nicitytestproject.databinding.NewsRecyclerViewItemBinding
import com.nicity.nicitytestproject.presentation.models.NewsVO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class FeedAdapter : ListAdapter<NewsVO, FeedAdapter.FeedViewHolder>(FeedDiffUtils()) {

    var newsClickImpl: NewsClick? = null

    class FeedViewHolder(
        private val binding: NewsRecyclerViewItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(news: NewsVO) {
            with(binding) {
                tvNewsTitle.text = news.title
                news.image.let {
                    CoroutineScope(Main).launch {
                        Glide.with(binding.root)
                            .load(news.image)
                            .placeholder(R.drawable.no_picture_placeholder)
                            .fitCenter()
                            .into(ivNewsPicture)
                    }
                }
            }
        }

        fun setNewsClickListener(newsClick: NewsClick?, news: NewsVO) {
            binding.root.setOnClickListener {
                newsClick?.handleNewsClick(news = news)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(
            binding = NewsRecyclerViewItemBinding
                .inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        with(holder) {
            bind(news = currentList[position])
            setNewsClickListener(newsClick = newsClickImpl, news = currentList[adapterPosition])
        }
    }

    private class FeedDiffUtils : DiffUtil.ItemCallback<NewsVO>() {

        override fun areItemsTheSame(oldItem: NewsVO, newItem: NewsVO): Boolean {
            return (oldItem.title + oldItem.image) == (newItem.title + newItem.image)
        }

        override fun areContentsTheSame(oldItem: NewsVO, newItem: NewsVO): Boolean {
            return oldItem == newItem
        }
    }
}

interface NewsClick {
    fun handleNewsClick(news: NewsVO)
}