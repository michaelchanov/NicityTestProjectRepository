package com.nicity.nicitytestproject.presentation.models

import android.os.Parcel
import android.os.Parcelable
import com.nicity.domain.models.News

data class NewsVO(
    val title: String, val image: String?,
    val description: String, val source: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString() ?: "",
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(image)
        parcel.writeString(description)
        parcel.writeString(source)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsVO> {

        override fun createFromParcel(parcel: Parcel): NewsVO {
            return NewsVO(parcel)
        }

        override fun newArray(size: Int): Array<NewsVO?> {
            return arrayOfNulls(size)
        }
    }
}

fun NewsVO.toNews(): News {
    return News(title = title, image = image,
        description = description, source = source)
}