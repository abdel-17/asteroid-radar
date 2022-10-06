package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageOfTheDay(
    @Json(name = "media_type") val mediaType: String,
    val title: String,
    val url: String
) {
    /**
     * True if [mediaType] is "image".
     */
    val isImage: Boolean
        get() = mediaType == "image"
}