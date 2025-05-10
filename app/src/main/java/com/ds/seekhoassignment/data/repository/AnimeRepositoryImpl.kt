package com.ds.seekhoassignment.data.repository

import android.util.Log
import com.ds.seekhoassignment.data.model.Data
import com.ds.seekhoassignment.data.networking.AnimeService
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val animeService: AnimeService
) : AnimeRepository {


    override suspend fun fetchAnimeList(): List<Data>? {
        val response = animeService.getAnimeList()
        Log.i("API -> ", "fetchAnimeList: $response")
        return if (response.isSuccessful) {
            response.body()?.data
        } else {
            Log.e("API -> ", "Error: ${response.errorBody()?.string()}")
            null
        }
    }
}

