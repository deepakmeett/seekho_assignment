package com.ds.seekhoassignment.data.viewModel

import androidx.lifecycle.viewModelScope
import com.ds.seekhoassignment.data.model.AnimeDetailResponse
import com.ds.seekhoassignment.data.model.Data
import com.ds.seekhoassignment.data.repository.AnimeRepository
import com.ds.seekhoassignment.data.utils.Resource
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
            repository.fetchAnimeList().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        setState { copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        setState {
                            copy(
                                isLoading = false,
                                animeListData = result.data
                            )
                        }
                    }

                    is Resource.Error -> {
                        setEffect {
                            AnimeUiEffect.ShowError(
                                if (result.message.isNullOrEmpty()) "Something went wrong" else result.message
                            )
                        }

                        setState {
                            copy(
                                isLoading = false,
                                animeListData = emptyList()
                            )
                        }
                    }
                }
            }
        }
    }


    private fun loadAnimeById(id: Int) {
        viewModelScope.launch {
            repository.fetchAnimeById(id).collect {
                when (it) {
                    is Resource.Loading -> {
                        setState { copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        setState {
                            copy(
                                isLoading = false,
                                animeDetailsData = it.data
                            )
                        }
                    }

                    is Resource.Error -> {
                        setEffect {
                            AnimeUiEffect.ShowError(
                                if (it.message.isNullOrEmpty()) "Something went wrong" else it.message
                            )
                        }
                        setState {
                            copy(
                                isLoading = false,
                                animeDetailsData = null
                            )
                        }
                    }
                }
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
    var animeDetailsData: AnimeDetailResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : UiState


sealed interface AnimeUiEffect : UiEffect {
    data class ShowError(val message: String) : AnimeUiEffect
}