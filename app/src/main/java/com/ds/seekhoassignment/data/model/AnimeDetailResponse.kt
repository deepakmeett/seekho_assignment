package com.ds.seekhoassignment.data.model

import com.google.gson.annotations.SerializedName


data class AnimeDetailResponse(
    @SerializedName("data")
    val data: Data
)
