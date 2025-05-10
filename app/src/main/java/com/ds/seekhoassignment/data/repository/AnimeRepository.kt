package com.ds.seekhoassignment.data.repository

import com.ds.seekhoassignment.data.model.AnimeDetailResponse
import com.ds.seekhoassignment.data.model.Data

interface AnimeRepository {
    suspend fun fetchAnimeList(): List<Data>?
    suspend fun fetchAnimeById(id: Int): AnimeDetailResponse?
}



