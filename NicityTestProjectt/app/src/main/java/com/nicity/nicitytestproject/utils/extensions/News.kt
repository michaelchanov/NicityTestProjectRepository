package com.nicity.nicitytestproject.utils.extensions

import com.nicity.domain.models.News
import com.nicity.nicitytestproject.presentation.models.NewsVO

fun News.toNewsVO(): NewsVO {
    return NewsVO(
        title = title, image = image,
        description = description, source = source
    )
}