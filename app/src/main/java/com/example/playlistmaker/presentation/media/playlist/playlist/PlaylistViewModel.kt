package com.example.playlistmaker.presentation.media.playlist.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.usecases.PlaylistInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists

    private val _trackAdditionStatus = MutableStateFlow<String?>(null)
    val trackAdditionStatus: StateFlow<String?> = _trackAdditionStatus

    init {
        loadPlaylists()
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect { playlists ->
                _playlists.value = playlists
            }
        }
    }

    fun addTrackToPlaylist(playlistId: Long, trackId: String, playlistName: String) {
        viewModelScope.launch {
            val isTrackAlreadyInPlaylist = playlistInteractor.isTrackInPlaylist(playlistId, trackId)

            if (isTrackAlreadyInPlaylist) {
                _trackAdditionStatus.value = "Трек уже добавлен в плейлист $playlistName"
            } else {
                playlistInteractor.addTrackToPlaylist(playlistId, trackId)
                _trackAdditionStatus.value = "Добавлено в плейлист $playlistName"
            }
        }
    }

    fun clearTrackAdditionStatus() {
        _trackAdditionStatus.value = null
    }

    override fun onCleared() {
        super.onCleared()
        loadPlaylists() // 🔹 Гарантируем актуальные данные при уничтожении VM
    }
}
