package com.example.playlistmaker.presentation.media.playlist.newplaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.usecases.PlaylistInteractor
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : CreatePlaylistViewModel(playlistInteractor) {

    private val _playlistData = MutableLiveData<Playlist>()
    val playlistData: LiveData<Playlist> = _playlistData

    fun setPlaylistData(playlist: Playlist) {
        _playlistData.value = playlist
    }

    fun updatePlaylist(name: String, description: String) {
        _playlistData.value?.let { currentPlaylist ->
            val updatedPlaylist = currentPlaylist.copy(
                name = name,
                description = description
            )

            viewModelScope.launch {
                playlistInteractor.updatePlaylist(updatedPlaylist)
            }
        }
    }

}