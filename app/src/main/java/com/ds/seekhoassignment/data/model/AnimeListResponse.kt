package com.ds.seekhoassignment.data.model

import com.google.gson.annotations.SerializedName


data class AnimeListResponse(
    @SerializedName("data")
    val data: List<Data>?,
    @SerializedName("pagination")
    val pagination: Pagination?
)