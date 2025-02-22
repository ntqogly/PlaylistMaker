package com.example.playlistmaker.presentation.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.IFavoriteTrackInteractor
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteTrackInteractor: IFavoriteTrackInteractor
) : ViewModel() {
    private val _favoriteTracks = MutableLiveData<List<Track>>()
    val favoriteTracks: LiveData<List<Track>> = _favoriteTracks

    fun loadFavoriteTracks() {
        viewModelScope.launch {
            favoriteTrackInteractor.getFavoriteTracks().collect { tracks ->
                _favoriteTracks.postValue(tracks)
            }
        }
    }


}