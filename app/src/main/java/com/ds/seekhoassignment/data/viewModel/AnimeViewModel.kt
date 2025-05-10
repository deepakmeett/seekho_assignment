package com.ds.seekhoassignment.data.viewModel

import androidx.lifecycle.viewModelScope
import com.ds.seekhoassignment.data.model.AnimeDetailResponse
import com.ds.seekhoassignment.data.model.Data
import com.ds.seekhoassignment.data.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository
) : BaseViewModel<AnimeUiEvent, AnimeUiState, AnimeUiEffect>() {


    override fun createInitialState(): AnimeUiState = AnimeUiState()

    override fun handleEvent(event: AnimeUiEvent) {
        when (event) {
            is AnimeUiEvent.LoadAnimeList -> {
                if (uiState.value.animeListData.isNullOrEmpty()) {
                    loadAnime()
                }
            }

            is AnimeUiEvent.LoadAnimeById -> {
                if (uiState.value.animeDetailsData == null) {
                    loadAnimeById(event.id)
                }
            }
        }
    }

    private fun loadAnime() {
        viewModelScope.launch {
            val result = repository.fetchAnimeList()
            setState {
                copy(
                    animeListData = result ?: emptyList()
                )
            }
        }
    }

    private fun loadAnimeById(id: Int) {
        viewModelScope.launch {
            val result = repository.fetchAnimeById(id)
            setState {
                copy(
                    animeDetailsData = result
                )
            }
        }
    }

}

sealed class AnimeUiEvent : UiEvent {
    object LoadAnimeList : AnimeUiEvent()
    data class LoadAnimeById(val id: Int) : AnimeUiEvent()
}


data class AnimeUiState(
    var animeListData: List<Data>? = emptyList(),
    var animeDetailsData: AnimeDetailResponse? = null
) : UiState

sealed class AnimeUiEffect : UiEffect {

}