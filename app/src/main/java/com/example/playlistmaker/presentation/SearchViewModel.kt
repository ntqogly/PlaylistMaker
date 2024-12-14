package com.example.playlistmaker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecases.SearchTrackUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val searchTrackUseCase: SearchTrackUseCase
) : ViewModel() {

    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> get() = _state

    fun searchTracks(query: String) {
        _state.value = SearchState.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
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
