package com.example.playlistmaker.presentation.player

sealed class PlayerState {
    data class Active(
        val playbackState: PlaybackState,
        val currentTime: String = "00:00"
    ) : PlayerState()

    enum class PlaybackState {
        PREPARED, PLAYING, PAUSED, STOPPED
    }
}