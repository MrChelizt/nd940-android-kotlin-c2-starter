package com.udacity.asteroidradar.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.DatabasePictureOfTheDay

@JsonClass(generateAdapter = true)
data class NetworkPictureOfTheDay(
    @Json(name = "media_type")
    val mediaType: String,
    @Json(name = "url")
    val imageUrl: String,
)

fun NetworkPictureOfTheDay.asDatabaseModel(): DatabasePictureOfTheDay {
    return DatabasePictureOfTheDay(
        mediaType = mediaType,
        imageUrl = imageUrl
    )
}