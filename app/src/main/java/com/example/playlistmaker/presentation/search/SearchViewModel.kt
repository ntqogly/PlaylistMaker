package com.example.playlistmaker.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.ISearchTrackUseCase
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class SearchViewModel(
    private val searchTrackUseCase: ISearchTrackUseCase,
    private val trackRepository: TrackRepository
) : ViewModel() {

    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> get() = _state

    fun searchTracks(query: String) {
        _state.value = SearchState.Loading
        searchTrackUseCase.executeAsync(query) { result ->
            _state.postValue(
                if (result.isNotEmpty()) {
                    SearchState.Success(result)
                } else {
                    SearchState.Empty
                }
            )
        }
    }

    fun clearSearch() {
        _state.value = SearchState.Initial
    }

    fun checkInternetConnection(): Boolean {
        return trackRepository.isInternetAvailable()
    }

    sealed class SearchState {
        data object Initial : SearchState()
        data object Loading : SearchState()
        data class Success(val tracks: List<Track>) : SearchState()
        data object Empty : SearchState()
    }
}