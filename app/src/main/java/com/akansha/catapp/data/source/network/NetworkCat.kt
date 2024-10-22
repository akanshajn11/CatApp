package com.akansha.catapp.data.source.network

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class NetworkCat(
    val breeds: List<NetworkBreed>?,
    val id: String,
    val url: String,
)


@JsonClass(generateAdapter = true)
data class NetworkBreed(
    val id: String,
    val name: String,
    val temperament: String?,
    val description: String?,
)
