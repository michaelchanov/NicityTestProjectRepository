package com.nicity.nicitytestproject.presentation.liked.adapters

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LikedNewsAdapter :
    ListAdapter<NewsVO, LikedNewsAdapter.LikedNewsViewHolder>(LikedNewsDiffUtils()) {

    var likedNewsClickImpl: LikedNewsClick? = null

    class LikedNewsViewHolder(
        private val binding: NewsRecyclerViewItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(news: NewsVO) {
            with(binding) {
                tvNewsTitle.text = news.title
                news.image.let {
                    CoroutineScope(Dispatchers.Main).launch {
                        Glide.with(binding.root)
                            .load(news.image)
                            .placeholder(R.drawable.no_picture_placeholder)
                            .fitCenter()
                            .into(ivNewsPicture)
                    }
                }
            }
        }

        fun setNewsClickListener(likedNewsClick: LikedNewsClick?, news: NewsVO) {
            binding.root.setOnClickListener {
                likedNewsClick?.handleLikedNewsClick(news = news)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedNewsViewHolder {
        return LikedNewsViewHolder(
            binding = NewsRecyclerViewItemBinding
                .inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: LikedNewsViewHolder, position: Int) {
        with(holder) {
            bind(news = currentList[position])
            setNewsClickListener(
                likedNewsClick = likedNewsClickImpl,
                news = currentList[adapterPosition]
            )
        }
    }

    private class LikedNewsDiffUtils : DiffUtil.ItemCallback<NewsVO>() {

        override fun areItemsTheSame(oldItem: NewsVO, newItem: NewsVO): Boolean {
            return (oldItem.title + oldItem.image) == (newItem.title + newItem.image)
        }

        override fun areContentsTheSame(oldItem: NewsVO, newItem: NewsVO): Boolean {
            return oldItem == newItem
        }
    }
}

interface LikedNewsClick {
    fun handleLikedNewsClick(news: NewsVO)
}