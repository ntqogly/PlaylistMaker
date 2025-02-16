package com.example.playlistmaker.presentation.player

sealed class PlayerState {
    data class Active(
        val playbackState: PlaybackState,
        val currentTime: String
    ) : PlayerState()

    enum class PlaybackState {
        PREPARED, PLAYING, PAUSED, STOPPED
    }
}