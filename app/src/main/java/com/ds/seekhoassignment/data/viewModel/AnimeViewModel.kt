package com.ds.seekhoassignment.data.viewModel

import androidx.lifecycle.viewModelScope
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

    init {
        viewModelScope.launch {
            loadAnime()
        }
    }
}

sealed class AnimeUiEvent : UiEvent {

}

data class AnimeUiState(
    var animeListData: List<Data>? = emptyList()
) : UiState

sealed class AnimeUiEffect : UiEffect {

}