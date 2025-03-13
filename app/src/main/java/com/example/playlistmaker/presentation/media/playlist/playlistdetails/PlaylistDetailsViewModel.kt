package com.example.playlistmaker.presentation.media.playlist.playlistdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistTrack
import com.example.playlistmaker.domain.usecases.PlaylistInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val _tracks = MutableStateFlow<List<PlaylistTrack>>(emptyList())
    val tracks = _tracks.asStateFlow()


    fun getPlaylistDetails(playlistId: Long): Flow<Playlist> {
        return playlistInteractor.getPlaylistById(playlistId).map { playlist ->
            playlist
        }
    }


    fun getTotalDuration(playlistId: Long): Flow<String> {
        return playlistInteractor.getTracksForPlaylist(playlistId).map { trackList ->
            val totalDurationMillis = trackList.sumOf { it.trackTimeMillis }
            val totalMinutes = totalDurationMillis / 60000
            formatMinutes(totalMinutes)
        }
    }

    private fun formatMinutes(minutes: Int): String {
        val lastDigit = (minutes % 10)
        val lastTwoDigits = (minutes % 100)

        return when {
            lastTwoDigits in 11..19 -> "$minutes минут"
            lastDigit == 1 -> "$minutes минута"
            lastDigit in 2..4 -> "$minutes минуты"
            else -> "$minutes минут"
        }
    }

    fun loadTracks(playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(playlistId).collectLatest { playlist ->
                val trackIdsOrder = playlist.trackIds

                playlistInteractor.getTracksForPlaylist(playlistId).collectLatest { trackList ->
                    _tracks.value = trackList.sortedByDescending { trackIdsOrder.indexOf(it.trackId.toString()) }
                }
            }
        }
    }


    fun deleteTrack(trackId: Long, playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(trackId, playlistId)
            loadTracks(playlistId)
        }
    }

    fun deletePlaylist(playlistId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlistId)
            onSuccess()
        }
    }
}
