package com.nicity.nicitytestproject.utils.extensions

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.setImage(url: String, context: Context) {
    Glide.with(context)
        .load(url)
        .into(this)
}