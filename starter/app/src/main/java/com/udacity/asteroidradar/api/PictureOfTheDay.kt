package com.udacity.asteroidradar.api

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PictureOfTheDay(
    @Json(name = "media_type")
    val mediaType: String,
    @Json(name = "url")
    val imageUrl: String,
) : Parcelable {
    val isImage
        get() = mediaType == "image"
}
