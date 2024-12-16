package com.example.playlistmaker.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.ISearchTrackUseCase
import com.example.playlistmaker.domain.models.Track

class SearchViewModel(
    private val searchTrackUseCase: ISearchTrackUseCase
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

    sealed class SearchState {
        object Initial : SearchState()
        object Loading : SearchState()
        data class Success(val tracks: List<Track>) : SearchState()
        object Empty : SearchState()
    }
}