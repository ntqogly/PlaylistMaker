package com.example.playlistmaker.presentation.media.playlist.playlistdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.usecases.PlaylistInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistDetailsViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

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


}
