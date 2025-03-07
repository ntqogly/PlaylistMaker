package com.example.playlistmaker.presentation.media.playlist.newplaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.usecases.PlaylistInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val _playlistName = MutableStateFlow("")
    val playlistName: StateFlow<String> = _playlistName

    private val _playlistDescription = MutableStateFlow("")
    val playlistDescription: StateFlow<String> = _playlistDescription

    private val _coverPath = MutableStateFlow<String?>(null)
    val coverPath: StateFlow<String?> = _coverPath

    private val _isCreateButtonEnabled = MutableStateFlow(false)
    val isCreateButtonEnabled: StateFlow<Boolean> = _isCreateButtonEnabled

    fun updateName(name: String) {
        _playlistName.value = name
        updateCreateButtonState()
    }

    fun updateDescription(description: String) {
        _playlistDescription.value = description
    }

    fun updateCoverPath(path: String?) {
        _coverPath.value = path
    }

    private fun updateCreateButtonState() {
        _isCreateButtonEnabled.value = _playlistName.value.isNotEmpty()
    }

    fun createPlaylist() {
        viewModelScope.launch {
            val newPlaylist = Playlist(
                name = _playlistName.value,
                description = _playlistDescription.value,
                coverPath = _coverPath.value
            )
            playlistInteractor.createPlaylist(newPlaylist)
        }
    }

}