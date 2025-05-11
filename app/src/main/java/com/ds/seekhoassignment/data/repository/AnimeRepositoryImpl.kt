package com.ds.seekhoassignment.data.repository

import android.util.Log
import com.ds.seekhoassignment.data.model.AnimeDetailResponse
import com.ds.seekhoassignment.data.model.Data
import com.ds.seekhoassignment.data.networking.AnimeService
import com.ds.seekhoassignment.data.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val animeService: AnimeService
) : AnimeRepository {


    override suspend fun fetchAnimeList(): Flow<Resource<List<Data>?>> = flow {
        emit(Resource.Loading)
        val response = animeService.getAnimeList()
        Log.i("API -> ", "fetchAnimeList: $response")
        try {
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()?.data))
            } else {
                Log.e("API -> ", "Error: ${response.errorBody()?.string()}")
                emit(Resource.Error(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Log.e("API -> ", "Error: $e")
            emit(Resource.Error(e.message))
        }
    }

    override suspend fun fetchAnimeById(id: Int): Flow<Resource<AnimeDetailResponse?>> = flow {
        emit(Resource.Loading)
        val response = animeService.getAnimeById(id)
        Log.i("API -> ", "fetchAnimeList: $response")

        try {
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()))
            } else {
                Log.e("API -> ", "Error: ${response.errorBody()?.string()}")
                emit(Resource.Error(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Log.e("API -> ", "Error: $e")
            emit(Resource.Error(response.errorBody()?.string()))
        }
    }
}

