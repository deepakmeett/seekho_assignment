package com.ds.seekhoassignment.data.networking

import com.ds.seekhoassignment.data.model.AnimeListResponse
import retrofit2.Response
import retrofit2.http.GET

interface AnimeService {
    @GET("v4/anime")
    suspend fun getAnimeList(): Response<AnimeListResponse>
}
