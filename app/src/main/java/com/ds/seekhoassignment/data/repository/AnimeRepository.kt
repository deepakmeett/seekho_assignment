package com.ds.seekhoassignment.data.repository

import com.ds.seekhoassignment.data.model.AnimeDetailResponse
import com.ds.seekhoassignment.data.model.Data
import com.ds.seekhoassignment.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun fetchAnimeList(): Flow<Resource<List<Data>?>>
    suspend fun fetchAnimeById(id: Int): Flow<Resource<AnimeDetailResponse?>>
}



