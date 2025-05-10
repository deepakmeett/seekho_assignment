package com.ds.seekhoassignment.data.networking

import com.ds.seekhoassignment.data.model.AnimeDetailResponse
import com.ds.seekhoassignment.data.model.AnimeListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimeService {
    @GET("v4/anime")
    suspend fun getAnimeList(): Response<AnimeListResponse>

    @GET("v4/anime/{anime_id}")
    suspend fun getAnimeById(@Path("anime_id") id: Int): Response<AnimeDetailResponse>
}
