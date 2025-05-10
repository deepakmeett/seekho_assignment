package com.ds.seekhoassignment.data.model


import com.google.gson.annotations.SerializedName

data class To(
    @SerializedName("day")
    val day: Int?,
    @SerializedName("month")
    val month: Int?,
    @SerializedName("year")
    val year: Int?
)